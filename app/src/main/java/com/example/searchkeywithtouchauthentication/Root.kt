package  com.example.searchkeywithtouchauthentication

import android.app.Application
import com.example.searchkeywithtouchauthentication.utils.Connectivity


class Root : Application() {

  override fun onCreate() {
        super.onCreate()
  Connectivity.init(this)
    }

}