package com.avast.metrics.scalaapi

import com.avast.metrics.api.{Naming, Monitor => JMonitor}
import com.avast.metrics.test.NoOpMonitor

trait Monitor extends AutoCloseable {
  def named(name: String): Monitor
  def named(name: String, name2: String, names: String*): Monitor
  def getName: String
  def meter(name: String): Meter
  def counter(name: String): Counter
  def timer(name: String): Timer
  def timerPair(name: String): TimerPair
  def gauge[A](name: String)(gauge: () => A): Gauge[A]
  def gauge[A](name: String, replaceExisting: Boolean)(gauge: () => A): Gauge[A]
  def histogram(name: String): Histogram
}

object Monitor {

  def apply(monitor: JMonitor): Monitor = new impl.MonitorImpl(monitor, Naming.defaultNaming())

  def noOp(): Monitor = {
    apply(NoOpMonitor.INSTANCE)
  }

}



