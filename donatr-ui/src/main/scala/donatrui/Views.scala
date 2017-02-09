package donatrui

import mhtml.Rx

import scala.util.Success
import scala.xml.Elem

object Views {
  def donatersView(): Rx[Elem] =
    Api.fetchDonaters
      .map {
        case None => <div>Loading</div>
        case Some(Success(donaters)) =>
          <div>
            {donaters.map(d => Components.DonaterComponent(d))}
          </div>
        case _ => <div>Failure!</div>
      }

  def donatablesView(): Rx[Elem] =
    Api.fetchDonatables
      .map {
        case None => <div>Loading</div>
        case Some(Success(donaters)) =>
          <div>
            {donaters.map(d => Components.DonatableComponent(d))}
          </div>
        case _ => <div>Failure!</div>
      }
}