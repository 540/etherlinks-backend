package v1.signature

import java.io.InputStream

import com.google.firebase.{FirebaseApp, FirebaseOptions}
import com.google.firebase.database._

object Firebase {
  private val credentials : InputStream = getClass.getResourceAsStream("/firebaseCredentials.json")
  private val options = new FirebaseOptions.Builder()
    .setDatabaseUrl("https://etherlinks-7503a.firebaseio.com")
    .setServiceAccount(credentials)
    .build()

  FirebaseApp.initializeApp(options)
  private val database = FirebaseDatabase.getInstance()
  def ref(path: String): DatabaseReference = database.getReference(path)
}
