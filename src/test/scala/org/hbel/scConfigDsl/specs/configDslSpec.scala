package org.hbel.scConfigDsl.specs

import org.hbel.scConfigDsl._
import org.scalatest._

class ConfigDslSpec extends FlatSpec with Matchers {
  "The test config" should "be foo and contain a bar" in {
    val cfg = new Config
    val bar = cfg / "foo" / "bar"
    bar should not be (None)
  }

  "The bar config" should "hold a text and a value" in {
    import org.hbel.scConfigDsl.Config
    val cfg = new Config
    val t = cfg / "foo" / "bar" % "text"
    t should be(Some("Text"))
    val v = cfg / "foo" / "bar" % "value"
    v should be(Some(15))
  }

  "The text config" should "not contain a foobar section" in {
    import org.hbel.scConfigDsl.Config
    val cfg = new Config
    val t = cfg / "foo" / "foobar"
    t should be(None)
  }

  "The text config at foo.bar" should "not contain a foobar key" in {
    import org.hbel.scConfigDsl.Config
    val cfg = new Config
    val t = cfg / "foo" / "bar" % "foobar"
    t should be(None)
  }

  "The bar config" should "return none if we ask for the wrong type" in {
    import org.hbel.scConfigDsl.Config
    val cfg = new Config
    a [ClassCastException] shouldBe thrownBy((cfg / "foo" / "bar" % "text").getOrElse(0))
    a [ClassCastException] shouldBe thrownBy((cfg / "foo" / "bar" % "value").getOrElse("none"))
  }

  "Children" should "return the child keys" in {
    import org.hbel.scConfigDsl.Config
    val cfg = new Config
    (cfg / "foo").children should contain("bar")
    val c : Seq[String] = (cfg / "foo" / "bar").children
    c should contain("text")
    c should contain("value")
  }
}
