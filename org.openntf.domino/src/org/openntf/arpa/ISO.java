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
	 * Carrier for ISO 3166-1 alpha 2 and alpha 3 code
	 * 
	 * @author Devin S. Olsonm (dolson@czarnowski.com)
	 * 
	 * @see "ISO 3166-1-alpha-2 code" http://www.iso.org/iso/country_names_and_code_elements
	 * 
	 */
	public static enum ISO3166 {
		AF("AFG", "Afghanistan"), AX("ALA", "Åland Islands"), AL("ALB", "Albania"), DZ("DZA", "Algeria"), AS("ASM", "American Samoa"), AD(
				"AND", "Andorra"), AO("AGO", "Angola"), AI("AIA", "Anguilla"), AQ("ATA", "Antarctica"), AG("ATG", "Antigua and Barbuda"), AR(
				"ARG", "Argentina"), AM("ARM", "Armenia"), AW("ABW", "Aruba"), AU("AUS", "Australia"), AT("AUT", "Austria"), AZ("AZE",
				"Azerbaijan"), BS("BHS", "Bahamas"), BH("BHR", "Bahrain"), BD("BGD", "Bangladesh"), BB("BRB", "Barbados"), BY("BLR",
				"Belarus"), BE("BEL", "Belgium"), BZ("BLZ", "Belize"), BJ("BEN", "Benin"), BM("BMU", "Bermuda"), BT("BTN", "Bhutan"), BO(
				"BOL", "Bolivia, Plurinational State of"), BQ("BES", "Bonaire, Sint Eustatius and Saba"), BA("BIH",
				"Bosnia and Herzegovina"), BW("BWA", "Botswana"), BV("BVT", "Bouvet Island"), BR("BRA", "Brazil"), IO("IOT",
				"British Indian Ocean Territory"), BN("BRN", "Brunei Darussalam"), BG("BGR", "Bulgaria"), BF("BFA", "Burkina Faso"), BI(
				"BDI", "Burundi"), KH("KHM", "Cambodia"), CM("CMR", "Cameroon"), CA("CAN", "Canada"), CV("CPV", "Cape Verde"), KY("CYM",
				"Cayman Islands"), CF("CAF", "Central African Republic"), TD("TCD", "Chad "), CL("CHL", "Chile"), CN("CHN", "China"), CX(
				"CXR", "Christmas Island"), CC("CCK", "Cocos (Keeling) Islands"), CO("COL", "Colombia"), KM("COM", "Comoros"), CG("COG",
				"Congo"), CD("COD", "Congo, the Democratic Republic of the"), CK("COK", "Cook Islands"), CR("CRI", "Costa Rica"), CI("CIV",
				"Côte d'Ivoire"), HR("HRV", "Croatia"), CU("CUB", "Cuba"), CW("CUW", "Curaçao"), CY("CYP", "Cyprus"), CZ("CZE",
				"Czech Republic"), DK("DNK", "Denmark"), DJ("DJI", "Djibouti"), DM("DMA", "Dominica"), DO("DOM", "Dominican Republic"), EC(
				"ECU", "Ecuador"), EG("EGY", "Egypt"), SV("SLV", "El Salvador"), GQ("GNQ", "Equatorial Guinea"), ER("ERI", "Eritrea"), EE(
				"EST", "Estonia"), ET("ETH", "Ethiopia"), FK("FLK", "Falkland Islands (Malvinas)"), FO("FRO", "Faroe Islands"), FJ("FJI",
				"Fiji"), FI("FIN", "Finland"), FR("FRA", "France"), GF("GUF", "French Guiana"), PF("PYF", "French Polynesia"), TF("ATF",
				"French Southern Territories"), GA("GAB", "Gabon"), GM("GMB", "Gambia"), GE("GEO", "Georgia"), DE("DEU", "Germany"), GH(
				"GHA", "Ghana"), GI("GIB", "Gibraltar"), GR("GRC", "Greece"), GL("GRL", "Greenland"), GD("GRD", "Grenada"), GP("GLP",
				"Guadeloupe"), GU("GUM", "Guam"), GT("GTM", "Guatemala"), GG("GGY", "Guernsey"), GN("GIN", "Guinea"), GW("GNB",
				"Guinea-Bissau"), GY("GUY", "Guyana"), HT("HTI", "Haiti"), HM("HMD", "Heard Island and McDonald Islands"), VA("VAT",
				"Holy See (Vatican City State)"), HN("HND", "Honduras"), HK("HKG", "Hong Kong"), HU("HUN", "Hungary"), IS("ISL", "Iceland"), IN(
				"IND", "India"), ID("IDN", "Indonesia"), IR("IRN", "Iran, Islamic Republic of"), IQ("IRQ", "Iraq"), IE("IRL", "Ireland"), IM(
				"IMN", "Isle of Man"), IL("ISR", "Israel"), IT("ITA", "Italy"), JM("JAM", "Jamaica"), JP("JPN", "Japan"), JE("JEY",
				"Jersey"), JO("JOR", "Jordan"), KZ("KAZ", "Kazakhstan"), KE("KEN", "Kenya"), KI("KIR", "Kiribati"), KP("PRK",
				"Korea, Democratic People's Republic of"), KR("KOR", "Korea, Republic of"), KW("KWT", "Kuwait"), KG("KGZ", "Kyrgyzstan"), LA(
				"LAO", "Lao People's Democratic Republic"), LV("LVA", "Latvia"), LB("LBN", "Lebanon"), LS("LSO", "Lesotho"), LR("LBR",
				"Liberia"), LY("LBY", "Libya"), LI("LIE", "Liechtenstein"), LT("LTU", "Lithuania"), LU("LUX", "Luxembourg"), MO("MAC",
				"Macao"), MK("MKD", "Macedonia, the former Yugoslav Republic of"), MG("MDG", "Madagascar"), MW("MWI", "Malawi"), MY("MYS",
				"Malaysia"), MV("MDV", "Maldives"), ML("MLI", "Mali"), MT("MLT", "Malta"), MH("MHL", "Marshall Islands"), MQ("MTQ",
				"Martinique"), MR("MRT", "Mauritania"), MU("MUS", "Mauritius"), YT("MYT", "Mayotte"), MX("MEX", "Mexico"), FM("FSM",
				"Micronesia, Federated States of"), MD("MDA", "Moldova, Republic of"), MC("MCO", "Monaco"), MN("MNG", "Mongolia"), ME(
				"MNE", "Montenegro"), MS("MSR", "Montserrat"), MA("MAR", "Morocco"), MZ("MOZ", "Mozambique"), MM("MMR", "Myanmar"), NA(
				"NAM", "Namibia"), NR("NRU", "Nauru"), NP("NPL", "Nepal"), NL("NLD", "Netherlands"), NC("NCL", "New Caledonia"), NZ("NZL",
				"New Zealand"), NI("NIC", "Nicaragua"), NE("NER", "Niger"), NG("NGA", "Nigeria"), NU("NIU", "Niue"), NF("NFK",
				"Norfolk Island"), MP("MNP", "Northern Mariana Islands"), NO("NOR", "Norway"), OM("OMN", "Oman"), PK("PAK", "Pakistan"), PW(
				"PLW", "Palau"), PS("PSE", "Palestine, State of"), PA("PAN", "Panama"), PG("PNG", "Papua New Guinea"), PY("PRY", "Paraguay"), PE(
				"PER", "Peru"), PH("PHL", "Philippines"), PN("PCN", "Pitcairn"), PL("POL", "Poland"), PT("PRT", "Portugal"), PR("PRI",
				"Puerto Rico"), QA("QAT", "Qatar"), RE("REU", "Réunion"), RO("ROU", "Romania"), RU("RUS", "Russian Federation"), RW("RWA",
				"Rwanda"), BL("BLM", "Saint Barthélemy"), SH("SHN", "Saint Helena, Ascension and Tristan da Cunha"), KN("KNA",
				"Saint Kitts and Nevis"), LC("LCA", "Saint Lucia"), MF("MAF", "Saint Martin (French part)"), PM("SPM",
				"Saint Pierre and Miquelon"), VC("VCT", "Saint Vincent and the Grenadines"), WS("WSM", "Samoa"), SM("SMR", "San Marino"), ST(
				"STP", "Sao Tome and Principe"), SA("SAU", "Saudi Arabia"), SN("SEN", "Senegal"), RS("SRB", "Serbia"), SC("SYC",
				"Seychelles"), SL("SLE", "Sierra Leone"), SG("SGP", "Singapore"), SX("SXM", "Sint Maarten (Dutch part)"), SK("SVK",
				"Slovakia"), SI("SVN", "Slovenia"), SB("SLB", "Solomon Islands"), SO("SOM", "Somalia"), ZA("ZAF", "South Africa"), GS(
				"SGS", "South Georgia and the South Sandwich Islands"), SS("SSD", "South Sudan"), ES("ESP", "Spain"), LK("LKA", "Sri Lanka"), SD(
				"SDN", "Sudan"), SR("SUR", "Suriname"), SJ("SJM", "Svalbard and Jan Mayen"), SZ("SWZ", "Swaziland"), SE("SWE", "Sweden"), CH(
				"CHE", "Switzerland"), SY("SYR", "Syrian Arab Republic"), TW("TWN", "Taiwan, Province of China"), TJ("TJK", "Tajikistan"), TZ(
				"TZA", "Tanzania, United Republic of"), TH("THA", "Thailand"), TL("TLS", "Timor-Leste"), TG("TGO", "Togo"), TK("TKL",
				"Tokelau"), TO("TON", "Tonga"), TT("TTO", "Trinidad and Tobago"), TN("TUN", "Tunisia"), TR("TUR", "Turkey"), TM("TKM",
				"Turkmenistan"), TC("TCA", "Turks and Caicos Islands"), TV("TUV", "Tuvalu"), UG("UGA", "Uganda"), UA("UKR", "Ukraine"), AE(
				"ARE", "United Arab Emirates"), GB("GBR", "United Kingdom"), US("USA", "United States"), UM("UMI",
				"United States Minor Outlying Islands"), UY("URY", "Uruguay"), UZ("UZB", "Uzbekistan"), VU("VUT", "Vanuatu"), VE("VEN",
				"Venezuela, Bolivarian Republic of"), VN("VNM", "Viet Nam"), VG("VGB", "Virgin Islands, British"), VI("VIR",
				"Virgin Islands, U.S."), WF("WLF", "Wallis and Futuna"), EH("ESH", "Western Sahara"), YE("YEM", "Yemen"), ZM("ZMB",
				"Zambia"), ZW("ZWE", "Zimbabwe");

		private String _country;
		private String _code3;

		public String getCountry() {
			return this._country;
		}

		private void setCountry(final String country) {
			this._country = country;
		}

		public String getCode2() {
			return this.name();
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
	};

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * public utility methods
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */

	public static ISO3166 getISO3166(final String code) {
		if (!isBlankString(code)) {
			if ((2 == code.length()) && code.matches("^[A-Z]+[A-Z]$")) {
				for (ISO3166 result : ISO3166.values()) {
					if (code.equals(result.getCode2())) {
						return result;
					}
				}
			} else if ((3 == code.length()) && code.matches("^[A-Z]+[A-Z]+[A-Z]$")) {
				for (ISO3166 result : ISO3166.values()) {
					if (code.equals(result.getCode3())) {
						return result;
					}
				}
			}
		}

		return null;
	}

	public static String getCountry(final String code) {
		ISO3166 temp = ISO.getISO3166(code);
		return (null == temp) ? "" : temp.getCountry();
	}

	public static boolean isCountryCode2(final String code) {
		ISO3166 temp = ISO.getISO3166(code);
		return (null == temp) ? false : temp.getCode2().equals(code);
	}

	public static boolean isCountryCode3(final String code) {
		ISO3166 temp = ISO.getISO3166(code);
		return (null == temp) ? false : temp.getCode3().equals(code);
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
