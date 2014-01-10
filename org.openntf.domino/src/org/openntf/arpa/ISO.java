/**
 * 
 */
package org.openntf.arpa;

import org.openntf.domino.utils.Strings;

/**
 * @author dolson
 * 
 */
public enum ISO {
	;

	/**
	 * Carrier for ISO 3166-1-alpha-2 code
	 * 
	 * @author Devin S. Olsonm (dolson@czarnowski.com)
	 * 
	 * @see "ISO 3166-1-alpha-2 code" http://www.iso.org/iso/country_names_and_code_elements
	 * 
	 */
	public static enum ISO3166 {
		AF("AFG", "AFGHANISTAN");

		private String _country;
		private String _code3;

		public String getCountry() {
			return this._country;
		}

		private void setCountry(final String country) {
			this._country = country;
		}

		public String getCode3() {
			return this._code3;
		}

		private void setCode3(final String code3) {
			this._code3 = code3;
		}

		private ISO3166(final String country, final String code3) {
			this.setCountry(country);
			this.setCode3(code3);
		}
	}

	/**
	 * Carrier for ISO 3166-1-alpha-2 code
	 * 
	 * @author Devin S. Olsonm (dolson@czarnowski.com)
	 * 
	 * @see "ISO 3166-1-alpha-2 code" http://www.iso.org/iso/country_names_and_code_elements
	 * 
	 */
	public static enum ISO3166_1_alpha_2 {
		AF("AFGHANISTAN"), AX("ÅLAND ISLANDS"), AL("ALBANIA"), DZ("ALGERIA"), AS("AMERICAN SAMOA"), AD("ANDORRA"), AO("ANGOLA"), AI(
				"ANGUILLA"), AQ("ANTARCTICA"), AG("ANTIGUA AND BARBUDA"), AR("ARGENTINA"), AM("ARMENIA"), AW("ARUBA"), AU("AUSTRALIA"), AT(
				"AUSTRIA"), AZ("AZERBAIJAN"), BS("BAHAMAS"), BH("BAHRAIN"), BD("BANGLADESH"), BB("BARBADOS"), BY("BELARUS"), BE("BELGIUM"), BZ(
				"BELIZE"), BJ("BENIN"), BM("BERMUDA"), BT("BHUTAN"), BO("BOLIVIA, PLURINATIONAL STATE OF"), BQ(
				"BONAIRE, SINT EUSTATIUS AND SABA"), BA("BOSNIA AND HERZEGOVINA"), BW("BOTSWANA"), BV("BOUVET ISLAND"), BR("BRAZIL"), IO(
				"BRITISH INDIAN OCEAN TERRITORY"), BN("BRUNEI DARUSSALAM"), BG("BULGARIA"), BF("BURKINA FASO"), BI("BURUNDI"), KH(
				"CAMBODIA"), CM("CAMEROON"), CA("CANADA"), CV("CAPE VERDE"), KY("CAYMAN ISLANDS"), CF("CENTRAL AFRICAN REPUBLIC"), TD(
				"CHAD"), CL("CHILE"), CN("CHINA"), CX("CHRISTMAS ISLAND"), CC("COCOS (KEELING) ISLANDS"), CO("COLOMBIA"), KM("COMOROS"), CG(
				"CONGO"), CD("CONGO, THE DEMOCRATIC REPUBLIC OF THE"), CK("COOK ISLANDS"), CR("COSTA RICA"), CI("CÔTE D'IVOIRE"), HR(
				"CROATIA"), CU("CUBA"), CW("CURAÇAO"), CY("CYPRUS"), CZ("CZECH REPUBLIC"), DK("DENMARK"), DJ("DJIBOUTI"), DM("DOMINICA"), DO(
				"DOMINICAN REPUBLIC"), EC("ECUADOR"), EG("EGYPT"), SV("EL SALVADOR"), GQ("EQUATORIAL GUINEA"), ER("ERITREA"), EE("ESTONIA"), ET(
				"ETHIOPIA"), FK("FALKLAND ISLANDS (MALVINAS)"), FO("FAROE ISLANDS"), FJ("FIJI"), FI("FINLAND"), FR("FRANCE"), GF(
				"FRENCH GUIANA"), PF("FRENCH POLYNESIA"), TF("FRENCH SOUTHERN TERRITORIES"), GA("GABON"), GM("GAMBIA"), GE("GEORGIA"), DE(
				"GERMANY"), GH("GHANA"), GI("GIBRALTAR"), GR("GREECE"), GL("GREENLAND"), GD("GRENADA"), GP("GUADELOUPE"), GU("GUAM"), GT(
				"GUATEMALA"), GG("GUERNSEY"), GN("GUINEA"), GW("GUINEA-BISSAU"), GY("GUYANA"), HT("HAITI"), HM(
				"HEARD ISLAND AND MCDONALD ISLANDS"), VA("HOLY SEE (VATICAN CITY STATE)"), HN("HONDURAS"), HK("HONG KONG"), HU("HUNGARY"), IS(
				"ICELAND"), IN("INDIA"), ID("INDONESIA"), IR("IRAN, ISLAMIC REPUBLIC OF"), IQ("IRAQ"), IE("IRELAND"), IM("ISLE OF MAN"), IL(
				"ISRAEL"), IT("ITALY"), JM("JAMAICA"), JP("JAPAN"), JE("JERSEY"), JO("JORDAN"), KZ("KAZAKHSTAN"), KE("KENYA"), KI(
				"KIRIBATI"), KP("KOREA, DEMOCRATIC PEOPLE'S REPUBLIC OF"), KR("KOREA, REPUBLIC OF"), KW("KUWAIT"), KG("KYRGYZSTAN"), LA(
				"LAO PEOPLE'S DEMOCRATIC REPUBLIC"), LV("LATVIA"), LB("LEBANON"), LS("LESOTHO"), LR("LIBERIA"), LY("LIBYA"), LI(
				"LIECHTENSTEIN"), LT("LITHUANIA"), LU("LUXEMBOURG"), MO("MACAO"), MK("MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF"), MG(
				"MADAGASCAR"), MW("MALAWI"), MY("MALAYSIA"), MV("MALDIVES"), ML("MALI"), MT("MALTA"), MH("MARSHALL ISLANDS"), MQ(
				"MARTINIQUE"), MR("MAURITANIA"), MU("MAURITIUS"), YT("MAYOTTE"), MX("MEXICO"), FM("MICRONESIA, FEDERATED STATES OF"), MD(
				"MOLDOVA, REPUBLIC OF"), MC("MONACO"), MN("MONGOLIA"), ME("MONTENEGRO"), MS("MONTSERRAT"), MA("MOROCCO"), MZ("MOZAMBIQUE"), MM(
				"MYANMAR"), NA("NAMIBIA"), NR("NAURU"), NP("NEPAL"), NL("NETHERLANDS"), NC("NEW CALEDONIA"), NZ("NEW ZEALAND"), NI(
				"NICARAGUA"), NE("NIGER"), NG("NIGERIA"), NU("NIUE"), NF("NORFOLK ISLAND"), MP("NORTHERN MARIANA ISLANDS"), NO("NORWAY"), OM(
				"OMAN"), PK("PAKISTAN"), PW("PALAU"), PS("PALESTINE, STATE OF"), PA("PANAMA"), PG("PAPUA NEW GUINEA"), PY("PARAGUAY"), PE(
				"PERU"), PH("PHILIPPINES"), PN("PITCAIRN"), PL("POLAND"), PT("PORTUGAL"), PR("PUERTO RICO"), QA("QATAR"), RE("RÉUNION"), RO(
				"ROMANIA"), RU("RUSSIAN FEDERATION"), RW("RWANDA"), BL("SAINT BARTHÉLEMY"), SH(
				"SAINT HELENA, ASCENSION AND TRISTAN DA CUNHA"), KN("SAINT KITTS AND NEVIS"), LC("SAINT LUCIA"), MF(
				"SAINT MARTIN (FRENCH PART)"), PM("SAINT PIERRE AND MIQUELON"), VC("SAINT VINCENT AND THE GRENADINES"), WS("SAMOA"), SM(
				"SAN MARINO"), ST("SAO TOME AND PRINCIPE"), SA("SAUDI ARABIA"), SN("SENEGAL"), RS("SERBIA"), SC("SEYCHELLES"), SL(
				"SIERRA LEONE"), SG("SINGAPORE"), SX("SINT MAARTEN (DUTCH PART)"), SK("SLOVAKIA"), SI("SLOVENIA"), SB("SOLOMON ISLANDS"), SO(
				"SOMALIA"), ZA("SOUTH AFRICA"), GS("SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS"), SS("SOUTH SUDAN"), ES("SPAIN"), LK(
				"SRI LANKA"), SD("SUDAN"), SR("SURINAME"), SJ("SVALBARD AND JAN MAYEN"), SZ("SWAZILAND"), SE("SWEDEN"), CH("SWITZERLAND"), SY(
				"SYRIAN ARAB REPUBLIC"), TW("TAIWAN, PROVINCE OF CHINA"), TJ("TAJIKISTAN"), TZ("TANZANIA, UNITED REPUBLIC OF"), TH(
				"THAILAND"), TL("TIMOR-LESTE"), TG("TOGO"), TK("TOKELAU"), TO("TONGA"), TT("TRINIDAD AND TOBAGO"), TN("TUNISIA"), TR(
				"TURKEY"), TM("TURKMENISTAN"), TC("TURKS AND CAICOS ISLANDS"), TV("TUVALU"), UG("UGANDA"), UA("UKRAINE"), AE(
				"UNITED ARAB EMIRATES"), GB("UNITED KINGDOM"), US("UNITED STATES"), UM("UNITED STATES MINOR OUTLYING ISLANDS"), UY(
				"URUGUAY"), UZ("UZBEKISTAN"), VU("VANUATU"), VE("VENEZUELA, BOLIVARIAN REPUBLIC OF"), VN("VIET NAM"), VG(
				"VIRGIN ISLANDS, BRITISH"), VI("VIRGIN ISLANDS, U.S."), WF("WALLIS AND FUTUNA"), EH("WESTERN SAHARA"), YE("YEMEN"), ZM(
				"ZAMBIA"), ZW("ZIMBABWE");

		private String _country;

		public String getCountry() {
			return this._country;
		}

		private void setCountry(final String country) {
			this._country = country;
		}

		private ISO3166_1_alpha_2(final String country) {
			this.setCountry(country);
		}
	}

	/**
	 * Carrier for ISO 3166-1-alpha-3 code
	 * 
	 * @author Devin S. Olsonm (dolson@czarnowski.com)
	 * 
	 * 
	 */
	public static enum ISO3166_1_alpha_3 {
		ABW("Aruba"), AFG("Afghanistan"), AGO("Angola"), AIA("Anguilla"), ALA("Åland Islands"), ALB("Albania"), AND("Andorra"), ARE(
				"United Arab Emirates"), ARG("Argentina"), ARM("Armenia"), ASM("American Samoa"), ATA("Antarctica"), ATF(
				"French Southern Territories"), ATG("Antigua and Barbuda"), AUS("Australia"), AUT("Austria"), AZE("Azerbaijan"), BDI(
				"Burundi"), BEL("Belgium"), BEN("Benin"), BES("Bonaire, Sint Eustatius and Saba"), BFA("Burkina Faso"), BGD("Bangladesh"), BGR(
				"Bulgaria"), BHR("Bahrain"), BHS("Bahamas"), BIH("Bosnia and Herzegovina"), BLM("Saint Barthélemy"), BLR("Belarus"), BLZ(
				"Belize"), BMU("Bermuda"), BOL("Bolivia, Plurinational State of"), BRA("Brazil"), BRB("Barbados"), BRN("Brunei Darussalam"), BTN(
				"Bhutan"), BVT("Bouvet Island"), BWA("Botswana"), CAF("Central African Republic"), CAN("Canada"), CCK(
				"Cocos (Keeling) Islands"), CHE("Switzerland"), CHL("Chile"), CHN("China"), CIV("Côte d'Ivoire"), CMR("Cameroon"), COD(
				"Congo, the Democratic Republic of the"), COG("Congo"), COK("Cook Islands"), COL("Colombia"), COM("Comoros"), CPV(
				"Cape Verde"), CRI("Costa Rica"), CUB("Cuba"), CUW("Curaçao"), CXR("Christmas Island"), CYM("Cayman Islands"), CYP("Cyprus"), CZE(
				"Czech Republic"), DEU("Germany"), DJI("Djibouti"), DMA("Dominica"), DNK("Denmark"), DOM("Dominican Republic"), DZA(
				"Algeria"), ECU("Ecuador"), EGY("Egypt"), ERI("Eritrea"), ESH("Western Sahara"), ESP("Spain"), EST("Estonia"), ETH(
				"Ethiopia"), FIN("Finland"), FJI("Fiji"), FLK("Falkland Islands (Malvinas)"), FRA("France"), FRO("Faroe Islands"), FSM(
				"Micronesia, Federated States of"), GAB("Gabon"), GBR("United Kingdom"), GEO("Georgia"), GGY("Guernsey"), GHA("Ghana"), GIB(
				"Gibraltar"), GIN("Guinea"), GLP("Guadeloupe"), GMB("Gambia"), GNB("Guinea-Bissau"), GNQ("Equatorial Guinea"), GRC("Greece"), GRD(
				"Grenada"), GRL("Greenland"), GTM("Guatemala"), GUF("French Guiana"), GUM("Guam"), GUY("Guyana"), HKG("Hong Kong"), HMD(
				"Heard Island and McDonald Islands"), HND("Honduras"), HRV("Croatia"), HTI("Haiti"), HUN("Hungary"), IDN("Indonesia"), IMN(
				"Isle of Man"), IND("India"), IOT("British Indian Ocean Territory"), IRL("Ireland"), IRN("Iran, Islamic Republic of"), IRQ(
				"Iraq"), ISL("Iceland"), ISR("Israel"), ITA("Italy"), JAM("Jamaica"), JEY("Jersey"), JOR("Jordan"), JPN("Japan"), KAZ(
				"Kazakhstan"), KEN("Kenya"), KGZ("Kyrgyzstan"), KHM("Cambodia"), KIR("Kiribati"), KNA("Saint Kitts and Nevis"), KOR(
				"Korea, Republic of"), KWT("Kuwait"), LAO("Lao People's Democratic Republic"), LBN("Lebanon"), LBR("Liberia"), LBY("Libya"), LCA(
				"Saint Lucia"), LIE("Liechtenstein"), LKA("Sri Lanka"), LSO("Lesotho"), LTU("Lithuania"), LUX("Luxembourg"), LVA("Latvia"), MAC(
				"Macao"), MAF("Saint Martin (French part)"), MAR("Morocco"), MCO("Monaco"), MDA("Moldova, Republic of"), MDG("Madagascar"), MDV(
				"Maldives"), MEX("Mexico"), MHL("Marshall Islands"), MKD("Macedonia, the former Yugoslav Republic of"), MLI("Mali"), MLT(
				"Malta"), MMR("Myanmar"), MNE("Montenegro"), MNG("Mongolia"), MNP("Northern Mariana Islands"), MOZ("Mozambique"), MRT(
				"Mauritania"), MSR("Montserrat"), MTQ("Martinique"), MUS("Mauritius"), MWI("Malawi"), MYS("Malaysia"), MYT("Mayotte"), NAM(
				"Namibia"), NCL("New Caledonia"), NER("Niger"), NFK("Norfolk Island"), NGA("Nigeria"), NIC("Nicaragua"), NIU("Niue"), NLD(
				"Netherlands"), NOR("Norway"), NPL("Nepal"), NRU("Nauru"), NZL("New Zealand"), OMN("Oman"), PAK("Pakistan"), PAN("Panama"), PCN(
				"Pitcairn"), PER("Peru"), PHL("Philippines"), PLW("Palau"), PNG("Papua New Guinea"), POL("Poland"), PRI("Puerto Rico"), PRK(
				"Korea, Democratic People's Republic of"), PRT("Portugal"), PRY("Paraguay"), PSE("Palestine, State of"), PYF(
				"French Polynesia"), QAT("Qatar"), REU("Réunion"), ROU("Romania"), RUS("Russian Federation"), RWA("Rwanda"), SAU(
				"Saudi Arabia"), SDN("Sudan"), SEN("Senegal"), SGP("Singapore"), SGS("South Georgia and the South Sandwich Islands"), SHN(
				"Saint Helena, Ascension and Tristan da Cunha"), SJM("Svalbard and Jan Mayen"), SLB("Solomon Islands"), SLE("Sierra Leone"), SLV(
				"El Salvador"), SMR("San Marino"), SOM("Somalia"), SPM("Saint Pierre and Miquelon"), SRB("Serbia"), SSD("South Sudan"), STP(
				"Sao Tome and Principe"), SUR("Suriname"), SVK("Slovakia"), SVN("Slovenia"), SWE("Sweden"), SWZ("Swaziland"), SXM(
				"Sint Maarten (Dutch part)"), SYC("Seychelles"), SYR("Syrian Arab Republic"), TCA("Turks and Caicos Islands"), TCD("Chad"), TGO(
				"Togo"), THA("Thailand"), TJK("Tajikistan"), TKL("Tokelau"), TKM("Turkmenistan"), TLS("Timor-Leste"), TON("Tonga"), TTO(
				"Trinidad and Tobago"), TUN("Tunisia"), TUR("Turkey"), TUV("Tuvalu"), TWN("Taiwan, Province of China"), TZA(
				"Tanzania, United Republic of"), UGA("Uganda"), UKR("Ukraine"), UMI("United States Minor Outlying Islands"), URY("Uruguay"), USA(
				"United States"), UZB("Uzbekistan"), VAT("Holy See (Vatican City State)"), VCT("Saint Vincent and the Grenadines"), VEN(
				"Venezuela, Bolivarian Republic of"), VGB("Virgin Islands, British"), VIR("Virgin Islands, U.S."), VNM("Viet Nam"), VUT(
				"Vanuatu"), WLF("Wallis and Futuna"), WSM("Samoa"), YEM("Yemen"), ZAF("South Africa"), ZMB("Zambia"), ZWE("Zimbabwe");
		;

		private String _country;

		public String getCountry() {
			return this._country;
		}

		private void setCountry(final String country) {
			this._country = country;
		}

		private ISO3166_1_alpha_3(final String country) {
			this.setCountry(country);
		}
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * public utility methods
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */

	public static ISO3166_1_alpha_2 getISO3166_1_alpha_2(final String code) {
		if ((!isBlankString(code)) && (2 == code.length()) && code.matches("^[A-Z]+[A-Z]$")) {
			for (ISO3166_1_alpha_2 result : ISO3166_1_alpha_2.values()) {
				if (code.equals(result.name())) {
					return result;
				}
			}
		}

		return null;
	}

	public static ISO3166_1_alpha_3 getISO3166_1_alpha_3(final String code) {
		if ((!isBlankString(code)) && (3 == code.length()) && code.matches("^[A-Z]+[A-Z]$")) {
			for (ISO3166_1_alpha_3 result : ISO3166_1_alpha_3.values()) {
				if (code.equals(result.name())) {
					return result;
				}
			}
		}

		return null;
	}

	public static String getCountry(final String code) {
		if (null != code) {
			switch (code.length()) {
			case 2: {
				ISO3166_1_alpha_2 temp = ISO.getISO3166_1_alpha_2(code);
				return (null == temp) ? "" : temp.getCountry();
			}
			case 3: {
				ISO3166_1_alpha_3 temp = ISO.getISO3166_1_alpha_3(code);
				return (null == temp) ? "" : temp.getCountry();
			}
			}
		}

		return "";
	}

	public static boolean isCountryCode2(final String code) {
		return (null != ISO.getISO3166_1_alpha_2(code));
	}

	public static boolean isCountryCode3(final String code) {
		return (null != ISO.getISO3166_1_alpha_3(code));
	}

	public static String toProperCase(final String source) {
		return (Strings.isBlankString(source)) ? "" : source.substring(0, 1).toUpperCase() + source.substring(1);
	}

	/**
	 * Determines if a string is null or blank
	 * 
	 * @param string
	 *            Source string to check for null or blank value.
	 * 
	 * @return Flag indicating if the source string is null or blank.
	 */
	public static boolean isBlankString(final String string) {
		return ((null == string) || (string.trim().length() < 1));
	}

}
