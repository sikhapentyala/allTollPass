package com.tcss559.alltollpass.config;

/**
 * @author sikha
 * This file defines the various services we will use for service composition
 */

public class AppConstants {
    private AppConstants(){}
    public static final String DEDUCT_AMOUNT = "/traveler/balance/debit";
    public static final String GET_VEHICLE_TYPE_BY_RFID = "/traveler/rfid";
    public static final String GET_TOLL_RATE_BY_AGENCY = "/toll/rates";
    public static final String CREATE_TRANSACTION = "/toll//transaction";
}
