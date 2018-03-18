package co.early.password123.feature.analytics

interface AnalyticsConstants {

    interface Events {

        interface InfoScreen {
            companion object {
                val SEARCH_FOR_PASSWORD_MGR = "InfoScreen_Click_PasswordMgr"
            }
        }

        interface Other {
            companion object {
                val LOGIN_SUCCESS = "Login_Success"
            }
        }
    }
}
