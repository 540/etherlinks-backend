package v1.signature

import scala.beans.BeanProperty

case class Signature(uri: String, transaction_id: String, date: Long) {
  def toBean = {
    val signature = new SignatureBean()
    signature.uri = uri
    signature.transaction_id = transaction_id
    signature.date = date
    signature
  }

}

class SignatureBean() {
  @BeanProperty var uri:String = null
  @BeanProperty var transaction_id:String = null
  @BeanProperty var date:Long = 0
}
