package frameless
package functions

import org.scalacheck.Prop
import org.scalacheck.Prop._

class UdfTest extends TypedDatasetSuite {

  test("one argument udf") {
    def prop[A: TypedEncoder, B: TypedEncoder, C: TypedEncoder]
    (data: Vector[X3[A, B, C]], f1: A => A): Prop = {
      val dataset = TypedDataset.create(data)
      val u1 = udf[X3[A, B, C], A, A](f1)
      val u2 = dataset.makeUDF(f1)
      val A = dataset.col[A]('a)

      val dataset21 = dataset.select(u1(A)).collect().run().toVector
      val dataset22 = dataset.select(u2(A)).collect().run().toVector
      val d = data.map(x => f1(x.a))

      (dataset21 ?= d) && (dataset22 ?= d)
    }

    check(forAll(prop[Int, Int, Int] _))
    check(forAll(prop[String, Int, Int] _))
    check(forAll(prop[Option[Int], X2[Double, Long], Int] _))
    check(forAll(prop[Option[Vector[String]], Int, Int] _))
  }

  test("multiple one argument udf") {
    def prop[A: TypedEncoder, B: TypedEncoder, C: TypedEncoder]
    (data: Vector[X3[A, B, C]], f1: A => A, f2: B => B, f3: C => C): Prop = {
      val dataset = TypedDataset.create(data)
      val u11 = udf[X3[A, B, C], A, A](f1)
      val u21 = udf[X3[A, B, C], B, B](f2)
      val u31 = udf[X3[A, B, C], C, C](f3)
      val u12 = dataset.makeUDF(f1)
      val u22 = dataset.makeUDF(f2)
      val u32 = dataset.makeUDF(f3)
      val A = dataset.col[A]('a)
      val B = dataset.col[B]('b)
      val C = dataset.col[C]('c)

      val dataset21 = dataset.select(u11(A), u21(B), u31(C)).collect().run().toVector
      val dataset22 = dataset.select(u12(A), u22(B), u32(C)).collect().run().toVector
      val d = data.map(x => (f1(x.a), f2(x.b), f3(x.c)))

      (dataset21 ?= d) && (dataset22 ?= d)
    }

    check(forAll(prop[Int, Int, Int] _))
    check(forAll(prop[String, Int, Int] _))
    check(forAll(prop[X3[Int, String, Boolean], Int, Int] _))
  }

  test("two argument udf") {
    def prop[A: TypedEncoder, B: TypedEncoder, C: TypedEncoder]
    (data: Vector[X3[A, B, C]], f1: (A, B) => C): Prop = {
      val dataset = TypedDataset.create(data)
      val u1 = udf[X3[A, B, C], A, B, C](f1)
      val u2 = dataset.makeUDF(f1)
      val A = dataset.col[A]('a)
      val B = dataset.col[B]('b)

      val dataset21 = dataset.select(u1(A, B)).collect().run().toVector
      val dataset22 = dataset.select(u2(A, B)).collect().run().toVector
      val d = data.map(x => f1(x.a, x.b))

      (dataset21 ?= d) && (dataset22 ?= d)
    }

    check(forAll(prop[Int, Int, Int] _))
    check(forAll(prop[String, Int, Int] _))
  }

  test("multiple two argument udf") {
    def prop[A: TypedEncoder, B: TypedEncoder, C: TypedEncoder]
    (data: Vector[X3[A, B, C]], f1: (A, B) => C, f2: (B, C) => A): Prop = {
      val dataset = TypedDataset.create(data)
      val u11 = udf[X3[A, B, C], A, B, C](f1)
      val u12 = dataset.makeUDF(f1)
      val u21 = udf[X3[A, B, C], B, C, A](f2)
      val u22 = dataset.makeUDF(f2)

      val A = dataset.col[A]('a)
      val B = dataset.col[B]('b)
      val C = dataset.col[C]('c)

      val dataset21 = dataset.select(u11(A, B), u21(B, C)).collect().run().toVector
      val dataset22 = dataset.select(u12(A, B), u22(B, C)).collect().run().toVector
      val d = data.map(x => (f1(x.a, x.b), f2(x.b, x.c)))

      (dataset21 ?= d) && (dataset22 ?= d)
    }

    check(forAll(prop[Int, Int, Int] _))
    check(forAll(prop[String, Int, Int] _))
  }

  test("three argument udf") {
    def prop[A: TypedEncoder, B: TypedEncoder, C: TypedEncoder]
    (data: Vector[X3[A, B, C]], f: (A, B, C) => C): Prop = {
      val dataset = TypedDataset.create(data)
      val u1 = udf[X3[A, B, C], A, B, C, C](f)
      val u2 = dataset.makeUDF(f)

      val A = dataset.col[A]('a)
      val B = dataset.col[B]('b)
      val C = dataset.col[C]('c)

      val dataset21 = dataset.select(u1(A, B, C)).collect().run().toVector
      val dataset22 = dataset.select(u2(A, B, C)).collect().run().toVector
      val d = data.map(x => f(x.a, x.b, x.c))

      (dataset21 ?= d) && (dataset22 ?= d)
    }

    check(forAll(prop[Int, Int, Int] _))
    check(forAll(prop[String, Int, Int] _))
  }

  test("four argument udf") {
    def prop[A: TypedEncoder, B: TypedEncoder, C: TypedEncoder]
    (data: Vector[X3[A, B, C]], f: (A, B, C, A) => C): Prop = {
      val dataset = TypedDataset.create(data)
      val u1 = udf[X3[A, B, C], A, B, C, A, C](f)
      val u2 = dataset.makeUDF(f)

      val A = dataset.col[A]('a)
      val B = dataset.col[B]('b)
      val C = dataset.col[C]('c)

      val dataset21 = dataset.select(u1(A, B, C, A)).collect().run().toVector
      val dataset22 = dataset.select(u2(A, B, C, A)).collect().run().toVector
      val d = data.map(x => f(x.a, x.b, x.c, x.a))

      (dataset21 ?= d) && (dataset22 ?= d)
    }

    check(forAll(prop[Int, Int, Int] _))
    check(forAll(prop[String, Int, Int] _))
  }

  test("five argument udf") {
    def prop[A: TypedEncoder, B: TypedEncoder, C: TypedEncoder, D: TypedEncoder, E: TypedEncoder]
    (data: Vector[X5[A, B, C, D, E]], f: (A, B, C, D, E) => C): Prop = {
      val dataset = TypedDataset.create(data)
      val u1 = udf[X5[A, B, C, D, E], A, B, C, D, E, C](f)
      val u2 = dataset.makeUDF(f)

      val A = dataset.col[A]('a)
      val B = dataset.col[B]('b)
      val C = dataset.col[C]('c)
      val D = dataset.col[D]('d)
      val E = dataset.col[E]('e)

      val dataset21 = dataset.select(u1(A, B, C, D, E)).collect().run().toVector
      val dataset22 = dataset.select(u2(A, B, C, D, E)).collect().run().toVector
      val d = data.map(x => f(x.a, x.b, x.c, x.d, x.e))

      (dataset21 ?= d) && (dataset22 ?= d)
    }

    check(forAll(prop[Int, Int, Int, Int, Int] _))
  }
}
