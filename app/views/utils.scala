package views

import play.api.mvc.{AnyContent, Request}


/**
  * Created by stanikol on 23.09.16.
  */
object Utils {

  def getParamFromRequest(name: String)(implicit request: Request[AnyContent]): Option[String] = {
    if(request.body.asFormUrlEncoded.isDefined)
      request.body.asFormUrlEncoded.get.getOrElse(name, Seq.empty[String]).headOption
    else if(request.body.asMultipartFormData.isDefined)
      request.body.asMultipartFormData.get.dataParts(name).headOption
    else
      request.getQueryString(name)
  }

}
