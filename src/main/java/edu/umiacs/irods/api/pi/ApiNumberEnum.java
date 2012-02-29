/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

/**
 * Enumeration for the different api operations for the API_REQ messages.
 * From lib/api/include/apiNumber.h
 * 
 * The last 4 fields are from apiTable.h and define the inputs and outputs 
 * for each operation
 * 
 * @author toaster
 */
public enum ApiNumberEnum
{

    /**
     * java specific entry for non-api-req commands
     */
    NO_API_NUMBER(0),
    FILE_CREATE_AN(500),
    FILE_OPEN_AN(501),
    FILE_WRITE_AN(502),
    FILE_CLOSE_AN(503),
    FILE_LSEEK_AN(504),
    FILE_READ_AN(505),
    FILE_UNLINK_AN(506),
    FILE_MKDIR_AN(507),
    FILE_CHMOD_AN(508),
    FILE_RMDIR_AN(509),
    FILE_STAT_AN(510),
    FILE_FSTAT_AN(511),
    FILE_FSYNC_AN(512),
    FILE_STAGE_AN(513),
    FILE_GET_FS_FREE_SPACE_AN(514),
    FILE_OPENDIR_AN(515),
    FILE_CLOSEDIR_AN(516),
    FILE_READDIR_AN(517),
    FILE_PUT_AN(518),
    FILE_GET_AN(519),
    FILE_CHKSUM_AN(520),
    CHK_N_V_PATH_PERM_AN(521),
    FILE_RENAME_AN(522),
    FILE_TRUNCATE_AN(523),
    DATA_OBJ_CREATE_AN(601, DataObjInp_PI.class, false, null, false),
    DATA_OBJ_OPEN_AN(602, DataObjInp_PI.class, false, null, false),
    DATA_OBJ_READ201_AN(603, DataObjReadInp_PI.class, false, null, true),
    DATA_OBJ_WRITE201_AN(604),
    DATA_OBJ_CLOSE201_AN(605, DataObjCloseInp_PI.class, false, null, false),
    /**
     * Ingest a data object into irods
     */
    DATA_OBJ_PUT_AN(606, DataObjInp_PI.class, true, PortalOprOut_PI.class, false),
    DATA_PUT_AN(607, DataOprInp_PI.class, false, PortalOprOut_PI.class, false),
    DATA_OBJ_GET_AN(608, DataObjInp_PI.class, false, PortalOprOut_PI.class, true),
    DATA_GET_AN(609, DataOprInp_PI.class, false, PortalOprOut_PI.class, false),
    DATA_OBJ_REPL_AN(610, DataObjInp_PI.class, false, TransStat_PI.class, false),
    DATA_COPY_AN(611),//, DataCopyInp_PI.class, false, null, false),
    DATA_OBJ_LSEEK201_AN(612),
    DATA_OBJ_COPY_AN(613),
    SIMPLE_QUERY_AN(614),
    DATA_OBJ_UNLINK_AN(615, DataObjInp_PI.class, false, null, false),
    COLL_CREATE201_AN(616, CollInp_PI.class, false, null, false),
    RM_COLL_OLD201_AN(617, CollInp_PI.class, false, null, false),
    REG_COLL201_AN(618, CollInp_PI.class, false, null, false),
    REG_DATA_OBJ_AN(619, DataObjInfo_PI.class, false, DataObjInfo_PI.class, false),
    UNREG_DATA_OBJ_AN(620),
    REG_REPLICA_AN(621),
    MOD_DATA_OBJ_META_AN(622),
    RULE_EXEC_SUBMIT_AN(623),
    RULE_EXEC_DEL_AN(624),
    EXEC_MY_RULE_AN(625),
    OPR_COMPLETE_AN(626, IntPi.class, false, null, false),
    DATA_OBJ_RENAME_AN(627),
    DATA_OBJ_RSYNC_AN(628, DataObjInp_PI.class, false, MsParamArray_PI.class, false),
    DATA_OBJ_CHKSUM_AN(629, DataObjInp_PI.class, false, STR_PI.class, false),
    PHY_PATH_REG_AN(630, DataObjInp_PI.class, false, null, false),
    DATA_OBJ_PHYMV_AN(631, DataObjInp_PI.class, false, null, false),
    DATA_OBJ_TRIM_AN(632, DataObjInp_PI.class, false, null, false),
    /**
     * Stat an object or directory
     */
    OBJ_STAT_AN(633, DataObjInp_PI.class, false, RodsObjStat_PI.class, false),
    EXEC_CMD_AN(634),
    BUN_SUB_CREATE_AN(635),
    BUN_SUB_OPEN_AN(636),
    BUN_SUB_READ_AN(637),
    BUN_SUB_WRITE_AN(638),
    BUN_SUB_CLOSE_AN(639),
    BUN_SUB_UNLINK_AN(640),
    BUN_SUB_STAT_AN(641),
    BUN_SUB_FSTAT_AN(642),
    BUN_SUB_LSEEK_AN(643),
    BUN_SUB_RENAME_AN(644),
    QUERY_SPEC_COLL_AN(645, DataObjInp_PI.class, false, GenQueryOut_PI.class, false),
    MOD_COLL201_AN(646, CollInp_PI.class, false, null, false),
    BUN_SUB_MKDIR_AN(647),
    BUN_SUB_RMDIR_AN(648),
    BUN_SUB_OPENDIR_AN(649),
    BUN_SUB_READDIR_AN(650),
    BUN_SUB_CLOSEDIR_AN(651),
    DATA_OBJ_TRUNCATE_AN(652, DataObjInp_PI.class, false, null, false),
    BUN_SUB_TRUNCATE_AN(653),
    COLL_REPL201_AN(662),
    RM_COLL201_AN(663),
    DATA_OBJ_CLOSE_AN(673,OpenedDataObjInp_PI.class,false,null,false),
    DATA_OBJ_READ_AN(675,OpenedDataObjInp_PI.class,false,null,true),
    DATA_OBJ_WRITE_AN(676,OpenedDataObjInp_PI.class, true,null,false),
    RM_COLL_AN(679,CollInpNew_PI.class,false,CollOprStat_PI.class,false),
    COLL_CREATE_AN(681,CollInpNew_PI.class,false,null,false),
    GET_MISC_SVR_INFO_AN(700, null, false, MiscSvrInfo_PI.class, false),
    GENERAL_ADMIN_AN(701, GeneralAdminInp_PI.class, false, null, false),
    GEN_QUERY_AN(702, GenQueryInp_PI.class, false, GenQueryOut_PI.class, false),
    AUTH_REQUEST_AN(703, null, false, AuthRequestOut_PI.class, false),
    AUTH_RESPONSE_AN(704, AuthResponseInp_PI.class, false, null, false),
    AUTH_CHECK_AN(705),
    MOD_AVU_METADATA_AN(706),
    MOD_ACCESS_CONTROL_AN(707),
    RULE_EXEC_MOD_AN(708),
    GET_TEMP_PASSWORD_AN(709),
    GENERAL_UPDATE_AN(710),
    GSI_AUTH_REQUEST_AN(711),
    OPEN_COLLECTION201_AN(712);

    //
    private int i;
    private boolean inBitstream,  outBitstream;
    private Class<? extends IRodsPI> inPack;
    private Class<? extends IRodsPI> outPack;

    <T extends IRodsPI, U extends IRodsPI> ApiNumberEnum(int i, Class<T> inPack, boolean inBits, 
            Class<U> outPack, boolean outBits)
    {
        this.inBitstream = inBits;
        this.outBitstream = outBits;
        this.inPack = inPack;
        this.outPack = outPack;
        this.i = i;

    }

    ApiNumberEnum(int i)
    {
        this.i = i;
    }

    public int getInt()
    {
        return i;
    }

    public Class<? extends IRodsPI> getInPI()
    {
        return inPack;
    }

    public boolean hasInputBitstream()
    {
        return inBitstream;
    }

    public Class<? extends IRodsPI> getOutPI()
    {
        return outPack;
    }

    public boolean hasOutputBitstream()
    {
        return outBitstream;
    }

    public static ApiNumberEnum valueOfString(String i)
    {
        return valueOf(Integer.parseInt(i));
    }

    public static ApiNumberEnum valueOf(int i)
    {
        switch (i)
        {
            case 0:
                return NO_API_NUMBER;
            case 500:
                return FILE_CREATE_AN;
            case 501:
                return FILE_OPEN_AN;
            case 502:
                return FILE_WRITE_AN;
            case 503:
                return FILE_CLOSE_AN;
            case 504:
                return FILE_LSEEK_AN;
            case 505:
                return FILE_READ_AN;
            case 506:
                return FILE_UNLINK_AN;
            case 507:
                return FILE_MKDIR_AN;
            case 508:
                return FILE_CHMOD_AN;
            case 509:
                return FILE_RMDIR_AN;
            case 510:
                return FILE_STAT_AN;
            case 511:
                return FILE_FSTAT_AN;
            case 512:
                return FILE_FSYNC_AN;
            case 513:
                return FILE_STAGE_AN;
            case 514:
                return FILE_GET_FS_FREE_SPACE_AN;
            case 515:
                return FILE_OPENDIR_AN;
            case 516:
                return FILE_CLOSEDIR_AN;
            case 517:
                return FILE_READDIR_AN;
            case 518:
                return FILE_PUT_AN;
            case 519:
                return FILE_GET_AN;
            case 520:
                return FILE_CHKSUM_AN;
            case 521:
                return CHK_N_V_PATH_PERM_AN;
            case 522:
                return FILE_RENAME_AN;
            case 523:
                return FILE_TRUNCATE_AN;
            case 601:
                return DATA_OBJ_CREATE_AN;
            case 602:
                return DATA_OBJ_OPEN_AN;
            case 603:
                return DATA_OBJ_READ201_AN;
            case 604:
                return DATA_OBJ_WRITE201_AN;
            case 605:
                return DATA_OBJ_CLOSE201_AN;
            case 606:
                return DATA_OBJ_PUT_AN;
            case 607:
                return DATA_PUT_AN;
            case 608:
                return DATA_OBJ_GET_AN;
            case 609:
                return DATA_GET_AN;
            case 610:
                return DATA_OBJ_REPL_AN;
            case 611:
                return DATA_COPY_AN;
            case 612:
                return DATA_OBJ_LSEEK201_AN;
            case 613:
                return DATA_OBJ_COPY_AN;
            case 614:
                return SIMPLE_QUERY_AN;
            case 615:
                return DATA_OBJ_UNLINK_AN;
            case 616:
                return COLL_CREATE201_AN;
            case 617:
                return RM_COLL_OLD201_AN;
            case 618:
                return REG_COLL201_AN;
            case 619:
                return REG_DATA_OBJ_AN;
            case 620:
                return UNREG_DATA_OBJ_AN;
            case 621:
                return REG_REPLICA_AN;
            case 622:
                return MOD_DATA_OBJ_META_AN;
            case 623:
                return RULE_EXEC_SUBMIT_AN;
            case 624:
                return RULE_EXEC_DEL_AN;
            case 625:
                return EXEC_MY_RULE_AN;
            case 626:
                return OPR_COMPLETE_AN;
            case 627:
                return DATA_OBJ_RENAME_AN;
            case 628:
                return DATA_OBJ_RSYNC_AN;
            case 629:
                return DATA_OBJ_CHKSUM_AN;
            case 630:
                return PHY_PATH_REG_AN;
            case 631:
                return DATA_OBJ_PHYMV_AN;
            case 632:
                return DATA_OBJ_TRIM_AN;
            case 633:
                return OBJ_STAT_AN;
            case 634:
                return EXEC_CMD_AN;
            case 635:
                return BUN_SUB_CREATE_AN;
            case 636:
                return BUN_SUB_OPEN_AN;
            case 637:
                return BUN_SUB_READ_AN;
            case 638:
                return BUN_SUB_WRITE_AN;
            case 639:
                return BUN_SUB_CLOSE_AN;
            case 640:
                return BUN_SUB_UNLINK_AN;
            case 641:
                return BUN_SUB_STAT_AN;
            case 642:
                return BUN_SUB_FSTAT_AN;
            case 643:
                return BUN_SUB_LSEEK_AN;
            case 644:
                return BUN_SUB_RENAME_AN;
            case 645:
                return QUERY_SPEC_COLL_AN;
            case 646:
                return MOD_COLL201_AN;
            case 647:
                return BUN_SUB_MKDIR_AN;
            case 648:
                return BUN_SUB_RMDIR_AN;
            case 649:
                return BUN_SUB_OPENDIR_AN;
            case 650:
                return BUN_SUB_READDIR_AN;
            case 651:
                return BUN_SUB_CLOSEDIR_AN;
            case 652:
                return DATA_OBJ_TRUNCATE_AN;
            case 653:
                return BUN_SUB_TRUNCATE_AN;
            case 662:
                return COLL_REPL201_AN;
            case 663:
                return RM_COLL201_AN;
            case 675:
                return DATA_OBJ_READ_AN;
            case 676:
                return DATA_OBJ_WRITE_AN;
            case 700:
                return GET_MISC_SVR_INFO_AN;
            case 701:
                return GENERAL_ADMIN_AN;
            case 702:
                return GEN_QUERY_AN;
            case 703:
                return AUTH_REQUEST_AN;
            case 704:
                return AUTH_RESPONSE_AN;
            case 705:
                return AUTH_CHECK_AN;
            case 706:
                return MOD_AVU_METADATA_AN;
            case 707:
                return MOD_ACCESS_CONTROL_AN;
            case 708:
                return RULE_EXEC_MOD_AN;
            case 709:
                return GET_TEMP_PASSWORD_AN;
            case 710:
                return GENERAL_UPDATE_AN;
            case 711:
                return GSI_AUTH_REQUEST_AN;
            case 712:
                return OPEN_COLLECTION201_AN;

            default:
                throw new IllegalArgumentException("unexpected int: " + i);
        }
    }
}
