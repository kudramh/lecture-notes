package com.kudram

object Main {

  def main(args: Array[String]): Unit = {
    akka.Main.main(Array(classOf[HelloAkka].getName))
  }

}