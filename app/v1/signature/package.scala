package v1

import play.api.i18n.Messages

/**
  * Package object for post.  This is a good place to put implicit conversions.
  */
package object signature {

  /**
    * Converts between PostRequest and Messages automatically.
    */
  implicit def requestToMessages[A](implicit r: SignatureRequest[A]): Messages = {
    r.messages
  }
}
