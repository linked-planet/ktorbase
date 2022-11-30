import com.typesafe.config.ConfigFactory

object BackendConfig {

    private val config = ConfigFactory.load("test.conf").getConfig("backend")
    val baseUrl: String = config.getString("baseUrl")
    val port: Int = config.getInt("port")
    val adminUsername: String = config.getString("adminUsername")
    val adminPassword: String = config.getString("adminPassword")

}
