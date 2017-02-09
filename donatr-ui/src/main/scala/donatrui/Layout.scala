package donatrui

import scala.xml.Elem

object Layout {
  val  donaterNavBar: Elem = {
    <ul class={DonatrStyles.nav.htmlClass}>
      <li class={DonatrStyles.navItem.htmlClass}>+user</li>
    </ul>
  }

  val donatableNavBar: Elem = {
    <ul class={DonatrStyles.nav.htmlClass}>
      <li class={DonatrStyles.navItem.htmlClass}>+item</li>
      <li class={DonatrStyles.navItem.htmlClass}>+€</li>
      <li class={DonatrStyles.navItem.htmlClass}>>funding</li>
    </ul>
  }

  val main: Elem =
    <div id="app">
      <h1 class={DonatrStyles.header.htmlClass}>donatr</h1>
      <hr class={DonatrStyles.hr.htmlClass}/>
      {States.currentView}
    </div>
}