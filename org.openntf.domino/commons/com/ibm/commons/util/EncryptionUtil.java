/*
 * © Copyright IBM Corp. 2012-2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package com.ibm.commons.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

//import com.ibm.commons.log.CommonsLogger;
//import com.ibm.commons.log.LogMgr;
import com.ibm.commons.util.io.base64.Base64InputStream;
import com.ibm.commons.util.io.base64.Base64OutputStream;

/**
 * Trivial utility class for transient encryption operations. This class should not be used for persistent storage of data as the key
 * generation and cipher used may be subject to change. This class is intended for securing temporary data, such as session-related
 * artefacts.
 * 
 * Usage:
 * 
 * <pre>
 * Key key = EncryptionUtil.generateKey();
 * byte[] data = &quot;Hello, World!&quot;.getBytes(&quot;UTF-8&quot;);
 * byte[] cyphertext = EncryptionUtil.encrypt(key, data);
 * byte[] plaintext = EncryptionUtil.decrypt(key, cyphertext);
 * if (data.length != plaintext.length) {
 * 	throw new IllegalStateException();
 * }
 * for (int i = 0; i &lt; data.length; i++) {
 * 	if (data[i] != plaintext[i]) {
 * 		throw new IllegalStateException();
 * 	}
 * }
 * </pre>
 * 
 * This class makes use of the Java cryptography extensions.
 * 
 * @ibm-not-published
 */
public class EncryptionUtil {

	private static final KeyGenerator KEY_GENERATOR = createKeyGenerator();
	private static final String ERROR_CODE = "ENC" + EncryptionUtil.class.hashCode(); //$NON-NLS-1$

	private static final String ALGORITHM = "DESede"; //DESede==Triple-DES //$NON-NLS-1$
	private static final String MODE = "ECB"; //$NON-NLS-1$
	private static final String PADDING = "PKCS5Padding"; //$NON-NLS-1$
	private static final String CIPHER = ALGORITHM + '/' + MODE + '/' + PADDING;

	private static final Logger log = Logger.getLogger(EncryptionUtil.class.getName());

	/**
	 * Creates a private key generator.
	 */
	private static final KeyGenerator createKeyGenerator() {
		try {
			KeyGenerator gen = KeyGenerator.getInstance(ALGORITHM); //$NON-NLS-1$
			return gen;
		} catch (Exception e) {
			try {
				if (log.isLoggable(Level.WARNING)) {
					String msg = "{0}: Error creating key generator for algorithm {1}"; // $NLE-EncryptionUtil.0Errorcreatingkeygeneratorforalgo-1$
					log.log(Level.WARNING, msg, new Object[] { ERROR_CODE, ALGORITHM });
				}
			} catch (Exception ex) {
				//FAILSAFE
				e.printStackTrace();
				ex.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * Creates a private key.
	 * 
	 * @return a new private key for use in symmetrical encryption.
	 * @throws GeneralSecurityException
	 */
	public static synchronized Key generateKey() throws GeneralSecurityException {
		if (KEY_GENERATOR == null) {
			String msg = "Key generator failed to initialize. For cause, search logs for error code {0}."; // $NLS-EncryptionUtil.KeygeneratorfailedtoinitializeFor-1$
			msg = StringUtil.format(msg, ERROR_CODE);
			throw new GeneralSecurityException(msg);
		}
		return KEY_GENERATOR.generateKey();
	}

	private static Cipher createCipher(final int cipherMode, final Key encryptionKey) throws GeneralSecurityException {
		if (encryptionKey == null) {
			throw new NullPointerException();
		}
		Cipher cipher = Cipher.getInstance(CIPHER); //$NON-NLS-1$
		if (cipherMode == Cipher.ENCRYPT_MODE) {
			cipher.init(cipherMode, encryptionKey);
		} else if (cipherMode == Cipher.DECRYPT_MODE) {
			cipher.init(cipherMode, encryptionKey);
		} else {
			throw new IllegalArgumentException("" + cipherMode); //$NON-NLS-1$
		}
		return cipher;
	}

	/**
	 * Encrypts a finite block of data. The key should be persisted for decryption.
	 * 
	 * @see generateKey()
	 * @param key
	 *            a private key for symmetric encryption
	 * @param data
	 *            the data to be encrypted
	 * @return the data in cyphertext form
	 * @throws GeneralSecurityException
	 */
	public static byte[] encrypt(final Key key, final byte[] data) throws GeneralSecurityException {
		if (key == null || data == null) {
			throw new IllegalArgumentException();
		}
		Cipher cipher = createCipher(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(data);
	}

	/**
	 * Decrypts a finite block of data.
	 * 
	 * @see generateKey()
	 * @param key
	 *            the private key used to encrypt the data
	 * @param encryptedData
	 *            the encrypted data
	 * @return the data in plaintext form
	 * @throws GeneralSecurityException
	 */
	public static byte[] decrypt(final Key key, final byte[] encryptedData) throws GeneralSecurityException {
		if (key == null || encryptedData == null) {
			throw new IllegalArgumentException();
		}
		Cipher cipher = createCipher(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(encryptedData);
	}

	/*public static void main(String[] args) throws Exception {
	    String test = "Hello, World!";
	    
	    {
	        Key key = generateKey();
	        byte[] data = test.getBytes("UTF-8");
	        byte[] cyphertext = encrypt(key, data);
	        byte[] plaintext = decrypt(key, cyphertext);
	        if(data.length!=plaintext.length) {
	            throw new IllegalStateException();
	        }
	        for(int i=0; i<data.length; i++) {
	            if(data[i]!=plaintext[i]) {
	                throw new IllegalStateException();
	            }
	        }
	        System.out.println("OK");
	    }
	    
	    {
	        Key key = generateKey();
	        System.out.println(test);
	        String cypherText = encryptString(key, test);
	        System.out.println(cypherText);
	        String plainText = decryptString(key, cypherText);
	        System.out.println(plainText);
	        if(!test.equals(plainText)) {
	            throw new IllegalStateException();
	        }
	    }
	}*/

	/**
	 * Encrypts a string.
	 */
	public static String encryptString(final Key key, final String plaintext) throws GeneralSecurityException {
		if (key == null || plaintext == null) {
			throw new NullPointerException();
		}

		try {
			byte[] data = plaintext.getBytes("UTF-8"); //$NON-NLS-1$
			byte[] cyphertext = encrypt(key, data);
			ByteArrayOutputStream barrout = new ByteArrayOutputStream(cyphertext.length * 3 / 2);
			Base64OutputStream base64 = new Base64OutputStream(barrout);
			base64.write(cyphertext);
			base64.flush();
			barrout.flush();
			//Base64 is ASCII safe
			String ret = barrout.toString("ASCII"); //$NON-NLS-1$
			return ret;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Decrypts a string encrypted with encryptString(key, plaintext).
	 */
	public static String decryptString(final Key key, final String cyphertext) throws GeneralSecurityException {
		if (key == null || cyphertext == null) {
			throw new NullPointerException();
		}

		try {
			byte[] base64 = cyphertext.getBytes("ASCII"); //$NON-NLS-1$
			ByteArrayInputStream bin = new ByteArrayInputStream(base64);
			Base64InputStream base64in = new Base64InputStream(bin);
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			byte[] barr = new byte[cyphertext.length()];
			while (true) {
				int r = base64in.read(barr);
				if (r <= 0)
					break;
				buffer.write(barr, 0, r);
			}
			byte[] encrypted = buffer.toByteArray();
			byte[] plaintext = decrypt(key, encrypted);
			String ret = new String(plaintext, "UTF-8"); //$NON-NLS-1$
			return ret;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
