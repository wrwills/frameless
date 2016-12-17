package frameless

import frameless.CollectTests.prop
import org.apache.spark.sql.SQLContext
import org.scalacheck.Prop
import org.scalacheck.Prop._
import scala.reflect.ClassTag

class CollectTests extends TypedDatasetSuite {
  test("collect()") {
    check(forAll(prop[X2[Int, Int]] _))
    check(forAll(prop[X2[String, String]] _))
    check(forAll(prop[X2[String, Int]] _))
    check(forAll(prop[X2[Long, Int]] _))

    check(forAll(prop[X2[X2[Int, String], Boolean]] _))
    check(forAll(prop[Tuple1[Option[Int]]] _))

    check(forAll(prop[Int] _))
    check(forAll(prop[Long] _))
    check(forAll(prop[Double] _))
    check(forAll(prop[Float] _))
    check(forAll(prop[Short] _))
    check(forAll(prop[Char] _))
    check(forAll(prop[Byte] _))
    check(forAll(prop[Boolean] _))
    check(forAll(prop[String] _))
    check(forAll(prop[SQLDate] _))
    check(forAll(prop[SQLTimestamp] _))
    check(forAll(prop[Option[Int]] _))
    check(forAll(prop[Option[Long]] _))
    check(forAll(prop[Option[Double]] _))
    check(forAll(prop[Option[Float]] _))
    check(forAll(prop[Option[Short]] _))
    check(forAll(prop[Option[Byte]] _))
    check(forAll(prop[Option[Boolean]] _))
    check(forAll(prop[Option[String]] _))
    check(forAll(prop[Option[SQLDate]] _))
    check(forAll(prop[Option[SQLTimestamp]] _))

    check(forAll(prop[Vector[Int]] _))
    check(forAll(prop[Option[Int]] _))
    check(forAll(prop[Vector[X2[Int, Int]]] _))
    check(forAll(prop[Map[String,Int]] _))
  }
}

object CollectTests {
  def prop[A: TypedEncoder : ClassTag](data: Vector[A])(implicit c: SQLContext): Prop =
    TypedDataset.create(data).collect().run().toVector ?= data
}
case class Foo(a: String, B: Map[String,Int])

class TestMap extends TypedDatasetSuite {
  import TypedEncoder._
  val testm = Seq(Foo("a",Map("aeu" -> 3)))
  //val testm = Seq(Map("aeu" -> 3))
  val test = Vector(1)
  implicit val sQLContext_ = sqlContext
  val t = TypedDataset.create( Seq(test) )
  //import sc..implicits._
  //import sQLContext_._
  val spark = session
  import spark.implicits._
  val ds = sqlContext.createDataset(testm)
  ds.show()
  //+---+-------------+
  //|  a|            B|
  //+---+-------------+
  //|  a|Map(aeu -> 3)|
  //+---+-------------+

  val tm = TypedDataset.create( testm )
  tm.show()
}