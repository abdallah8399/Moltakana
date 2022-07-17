package com.wingy.moltakana.Objects;

public class MemberPermission {
    private String userKey;
    public Boolean block_deviceB, stopB, fireB, micB, delete_textB, general_messageB,
            cancel_blockB, logout_historyB, account_managementB, manage_memberB,
            manage_adminB, manage_super_adminB, manage_masterB, manage_roomB,
            supervisors_reportB, close_deviceB;

    public MemberPermission(String userKey, Boolean block_deviceB, Boolean stopB, Boolean fireB, Boolean micB,
                            Boolean delete_textB, Boolean general_messageB,
                            Boolean cancel_blockB, Boolean logout_historyB, Boolean account_managementB,
                            Boolean manage_memberB,
                            Boolean manage_adminB, Boolean manage_super_adminB,
                            Boolean manage_masterB, Boolean manage_roomB,
                            Boolean supervisors_reportB, Boolean close_deviceB) {
        this.userKey = userKey;
        this.block_deviceB = block_deviceB;
        this.stopB = stopB;
        this.fireB = fireB;
        this.micB = micB;
        this.delete_textB = delete_textB;
        this.general_messageB = general_messageB;
        this.cancel_blockB = cancel_blockB;
        this.logout_historyB = logout_historyB;
        this.account_managementB = account_managementB;
        this.manage_memberB = manage_memberB;
        this.manage_adminB = manage_adminB;
        this.manage_super_adminB = manage_super_adminB;
        this.manage_masterB = manage_masterB;
        this.manage_roomB = manage_roomB;
        this.supervisors_reportB = supervisors_reportB;
        this.close_deviceB = close_deviceB;
    }
}
