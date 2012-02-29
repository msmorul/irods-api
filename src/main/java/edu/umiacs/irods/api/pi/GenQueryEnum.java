/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

/**
 * Enum for items in
 * lib/core/include/rodsGenQuery.h
 *
 * @author toaster
 */
public enum GenQueryEnum
{

    COL_ZONE_ID(101),
    COL_ZONE_NAME(102),
    COL_USER_ID(201),
    COL_USER_NAME(202),
    COL_USER_TYPE(203),
    COL_USER_ZONE(204),
    COL_USER_DN(205),
    COL_USER_INFO(206),
    COL_USER_COMMENT(207),
    COL_USER_CREATE_TIME(208),
    COL_USER_MODIFY_TIME(209),
    COL_R_RESC_ID(301),
    COL_R_RESC_NAME(302),
    COL_R_ZONE_NAME(303),
    COL_R_TYPE_NAME(304),
    COL_R_CLASS_NAME(305),
    COL_R_LOC(306),
    COL_R_VAULT_PATH(307),
    COL_R_FREE_SPACE(308),
    COL_R_RESC_INFO(309),
    COL_R_RESC_COMMENT(310),
    COL_R_CREATE_TIME(311),
    COL_R_MODIFY_TIME(312),
    COL_D_DATA_ID(401),
    COL_D_COLL_ID(402),
    COL_DATA_NAME(403),
    COL_DATA_REPL_NUM(404),
    COL_DATA_VERSION(405),
    COL_DATA_TYPE_NAME(406),
    COL_DATA_SIZE(407),
    COL_D_RESC_GROUP_NAME(408),
    COL_D_RESC_NAME(409),
    COL_D_DATA_PATH(410),
    COL_D_OWNER_NAME(411),
    COL_D_OWNER_ZONE(412),
    COL_D_REPL_STATUS(413),
    COL_D_DATA_STATUS(414),
    COL_D_DATA_CHECKSUM(415),
    COL_D_EXPIRY(416),
    COL_D_MAP_ID(417),
    COL_D_COMMENTS(418),
    COL_D_CREATE_TIME(419),
    COL_D_MODIFY_TIME(420),
    COL_COLL_ID(500),
    COL_COLL_NAME(501),
    COL_COLL_PARENT_NAME(502),
    COL_COLL_OWNER_NAME(503),
    COL_COLL_OWNER_ZONE(504),
    COL_COLL_MAP_ID(505),
    COL_COLL_INHERITANCE(506),
    COL_COLL_COMMENTS(507),
    COL_COLL_CREATE_TIME(508),
    COL_COLL_MODIFY_TIME(509),
    COL_COLL_TYPE(510),
    COL_COLL_INFO1(511),
    COL_COLL_INFO2(512),
    COL_META_DATA_ATTR_NAME(600),
    COL_META_DATA_ATTR_VALUE(601),
    COL_META_DATA_ATTR_UNITS(602),
    COL_META_DATA_ATTR_ID(603),
    COL_META_COLL_ATTR_NAME(610),
    COL_META_COLL_ATTR_VALUE(611),
    COL_META_COLL_ATTR_UNITS(612),
    COL_META_COLL_ATTR_ID(613),
    COL_META_NAMESPACE_COLL(620),
    COL_META_NAMESPACE_DATA(621),
    COL_META_NAMESPACE_RESC(622),
    COL_META_NAMESPACE_USER(623),
    COL_META_RESC_ATTR_NAME(630),
    COL_META_RESC_ATTR_VALUE(631),
    COL_META_RESC_ATTR_UNITS(632),
    COL_META_RESC_ATTR_ID(633),
    COL_META_USER_ATTR_NAME(640),
    COL_META_USER_ATTR_VALUE(641),
    COL_META_USER_ATTR_UNITS(642),
    COL_META_USER_ATTR_ID(643),
    COL_DATA_ACCESS_TYPE(700),
    COL_DATA_ACCESS_NAME(701),
    COL_DATA_TOKEN_NAMESPACE(702),
    COL_DATA_ACCESS_USER_ID(703),
    COL_DATA_ACCESS_DATA_ID(704),
    COL_RESC_GROUP_RESC_ID(800),
    COL_RESC_GROUP_NAME(801),
    COL_USER_GROUP_ID(900),
    COL_USER_GROUP_NAME(901),
    COL_RULE_EXEC_ID(1000),
    COL_RULE_EXEC_NAME(1001),
    COL_RULE_EXEC_REI_FILE_PATH(1002),
    COL_RULE_EXEC_USER_NAME(1003),
    COL_RULE_EXEC_ADDRESS(1004),
    COL_RULE_EXEC_TIME(1005),
    COL_RULE_EXEC_FREQUENCY(1006),
    COL_RULE_EXEC_PRIORITY(1007),
    COL_RULE_EXEC_ESTIMATED_EXE_TIME(1008),
    COL_RULE_EXEC_NOTIFICATION_ADDR(1009),
    COL_RULE_EXEC_LAST_EXE_TIME(1010),
    COL_RULE_EXEC_STATUS(1011),
    COL_TOKEN_NAMESPACE(1100),
    COL_TOKEN_ID(1101),
    COL_TOKEN_NAME(1102),
    COL_TOKEN_VALUE(1103),
    COL_TOKEN_VALUE2(1104),
    COL_TOKEN_VALUE3(1105),
    COL_TOKEN_COMMENT(1106);
    private int i;

    GenQueryEnum(int i)
    {
        this.i = i;
    }

    public int getInt()
    {
        return i;
    }
    
    public static GenQueryEnum valueOf(int i)
    {
        switch (i)
        {
            case 101:
                return COL_ZONE_ID;
            case 102:
                return COL_ZONE_NAME;
            case 201:
                return COL_USER_ID;
            case 202:
                return COL_USER_NAME;
            case 203:
                return COL_USER_TYPE;
            case 204:
                return COL_USER_ZONE;
            case 205:
                return COL_USER_DN;
            case 206:
                return COL_USER_INFO;
            case 207:
                return COL_USER_COMMENT;
            case 208:
                return COL_USER_CREATE_TIME;
            case 209:
                return COL_USER_MODIFY_TIME;
            case 301:
                return COL_R_RESC_ID;
            case 302:
                return COL_R_RESC_NAME;
            case 303:
                return COL_R_ZONE_NAME;
            case 304:
                return COL_R_TYPE_NAME;
            case 305:
                return COL_R_CLASS_NAME;
            case 306:
                return COL_R_LOC;
            case 307:
                return COL_R_VAULT_PATH;
            case 308:
                return COL_R_FREE_SPACE;
            case 309:
                return COL_R_RESC_INFO;
            case 310:
                return COL_R_RESC_COMMENT;
            case 311:
                return COL_R_CREATE_TIME;
            case 312:
                return COL_R_MODIFY_TIME;
            case 401:
                return COL_D_DATA_ID;
            case 402:
                return COL_D_COLL_ID;
            case 403:
                return COL_DATA_NAME;
            case 404:
                return COL_DATA_REPL_NUM;
            case 405:
                return COL_DATA_VERSION;
            case 406:
                return COL_DATA_TYPE_NAME;
            case 407:
                return COL_DATA_SIZE;
            case 408:
                return COL_D_RESC_GROUP_NAME;
            case 409:
                return COL_D_RESC_NAME;
            case 410:
                return COL_D_DATA_PATH;
            case 411:
                return COL_D_OWNER_NAME;
            case 412:
                return COL_D_OWNER_ZONE;
            case 413:
                return COL_D_REPL_STATUS;
            case 414:
                return COL_D_DATA_STATUS;
            case 415:
                return COL_D_DATA_CHECKSUM;
            case 416:
                return COL_D_EXPIRY;
            case 417:
                return COL_D_MAP_ID;
            case 418:
                return COL_D_COMMENTS;
            case 419:
                return COL_D_CREATE_TIME;
            case 420:
                return COL_D_MODIFY_TIME;
            case 500:
                return COL_COLL_ID;
            case 501:
                return COL_COLL_NAME;
            case 502:
                return COL_COLL_PARENT_NAME;
            case 503:
                return COL_COLL_OWNER_NAME;
            case 504:
                return COL_COLL_OWNER_ZONE;
            case 505:
                return COL_COLL_MAP_ID;
            case 506:
                return COL_COLL_INHERITANCE;
            case 507:
                return COL_COLL_COMMENTS;
            case 508:
                return COL_COLL_CREATE_TIME;
            case 509:
                return COL_COLL_MODIFY_TIME;
            case 510:
                return COL_COLL_TYPE;
            case 511:
                return COL_COLL_INFO1;
            case 512:
                return COL_COLL_INFO2;
            case 600:
                return COL_META_DATA_ATTR_NAME;
            case 601:
                return COL_META_DATA_ATTR_VALUE;
            case 602:
                return COL_META_DATA_ATTR_UNITS;
            case 603:
                return COL_META_DATA_ATTR_ID;
            case 610:
                return COL_META_COLL_ATTR_NAME;
            case 611:
                return COL_META_COLL_ATTR_VALUE;
            case 612:
                return COL_META_COLL_ATTR_UNITS;
            case 613:
                return COL_META_COLL_ATTR_ID;
            case 620:
                return COL_META_NAMESPACE_COLL;
            case 621:
                return COL_META_NAMESPACE_DATA;
            case 622:
                return COL_META_NAMESPACE_RESC;
            case 623:
                return COL_META_NAMESPACE_USER;
            case 630:
                return COL_META_RESC_ATTR_NAME;
            case 631:
                return COL_META_RESC_ATTR_VALUE;
            case 632:
                return COL_META_RESC_ATTR_UNITS;
            case 633:
                return COL_META_RESC_ATTR_ID;
            case 640:
                return COL_META_USER_ATTR_NAME;
            case 641:
                return COL_META_USER_ATTR_VALUE;
            case 642:
                return COL_META_USER_ATTR_UNITS;
            case 643:
                return COL_META_USER_ATTR_ID;
            case 700:
                return COL_DATA_ACCESS_TYPE;
            case 701:
                return COL_DATA_ACCESS_NAME;
            case 702:
                return COL_DATA_TOKEN_NAMESPACE;
            case 703:
                return COL_DATA_ACCESS_USER_ID;
            case 704:
                return COL_DATA_ACCESS_DATA_ID;
            case 800:
                return COL_RESC_GROUP_RESC_ID;
            case 801:
                return COL_RESC_GROUP_NAME;
            case 900:
                return COL_USER_GROUP_ID;
            case 901:
                return COL_USER_GROUP_NAME;
            case 1000:
                return COL_RULE_EXEC_ID;
            case 1001:
                return COL_RULE_EXEC_NAME;
            case 1002:
                return COL_RULE_EXEC_REI_FILE_PATH;
            case 1003:
                return COL_RULE_EXEC_USER_NAME;
            case 1004:
                return COL_RULE_EXEC_ADDRESS;
            case 1005:
                return COL_RULE_EXEC_TIME;
            case 1006:
                return COL_RULE_EXEC_FREQUENCY;
            case 1007:
                return COL_RULE_EXEC_PRIORITY;
            case 1008:
                return COL_RULE_EXEC_ESTIMATED_EXE_TIME;
            case 1009:
                return COL_RULE_EXEC_NOTIFICATION_ADDR;
            case 1010:
                return COL_RULE_EXEC_LAST_EXE_TIME;
            case 1011:
                return COL_RULE_EXEC_STATUS;
            case 1100:
                return COL_TOKEN_NAMESPACE;
            case 1101:
                return COL_TOKEN_ID;
            case 1102:
                return COL_TOKEN_NAME;
            case 1103:
                return COL_TOKEN_VALUE;
            case 1104:
                return COL_TOKEN_VALUE2;
            case 1105:
                return COL_TOKEN_VALUE3;
            case 1106:
                return COL_TOKEN_COMMENT;
            default:
                throw new IllegalArgumentException("No value for number: " + i);
        }
    }
}
