package org.hbel.scConfigDsl {

  import com.typesafe.scalalogging._
  import com.typesafe.config.{ConfigFactory, Config => TypesafeConfig}

  object Config {

    import scala.reflect.runtime.universe._

    /**
      * Convenience functions that allow to directly work with Option[Config]
      * @param cfg
      */
    implicit class CfgOption(private val cfg: Option[Config]) extends AnyRef {
      /**
        * Get a sub-section of the config
        * @param p sub-section name
        * @return configuration instance representing the sub-section
        */
      def /(p: String): Option[Config] = cfg match {
        case Some(x) => x / p
        case _ => None
      }

      /**
        * Get a configuration value. Keep in mind that due to type erasure,
        * this function will not fail for a wrong type parameter, but getting
        * the value later will throw a runtime exception if you use the wrong type.
        * @param p name of the configuration value
        * @tparam T type of the configuration value
        * @return value
        */
      def %[T: TypeTag](p: String): Option[T] = cfg match {
        case Some(x) => x % p
        case _ => None
      }

      /**
        * Get the names of the direct children of this section (sub-sections and values)
        * @return
        */
      def children : scala.Seq[String] = cfg match {
        case None => Seq()
        case Some(c) => c.children
      }
    }

  }

  /**
    * Wrapper for the java configuration class
    * @param config java configuration instance
    */
  class Config(private val config: TypesafeConfig) extends LazyLogging {

    import scala.reflect.runtime.universe._

    /**
      * Parameterless constructor. This is the constructor you should usually call to create the config.
      * @return New config instance
      */
    def this() = this(ConfigFactory.load)

    /**
      * As in the java version, provide a fallback configuration
      * @param that fallback configuration
      * @return combined configuration
      */
    def withFallback(that: Config): Config = new Config(this.config.withFallback(that.config))

    /**
      * Get a sub-section of the config
      * @param p sub-section name
      * @return configuration instance representing the sub-section
      */
    def /(p: String): Option[Config] = {
      logger.trace(s"Retrieving sub-config $p")
      if (!config.hasPath(p)) return None
      Some(new Config(config.getConfig(p.toString)))
    }

    /**
      * Get a configuration value. Keep in mind that due to type erasure,
      * this function will not fail for a wrong type parameter, but getting
      * the value later will throw a runtime exception if you use the wrong type.
      * @param p name of the configuration value
      * @tparam T type of the configuration value
      * @return value
      */
    def %[T: TypeTag](p: String): Option[T] = {
      logger.trace(s"Retrieving value $p")
      if (!config.hasPath(p)) return None
      val o: Object = config.getValue(p.toString).unwrapped
      o match {
        case elem: T => Some(elem)
        case _ => None
      }
    }

    /**
      * Get the names of the direct children of this section (sub-sections and values)
      * @return
      */
    def children : scala.Seq[String] = {
      import scala.collection.JavaConverters.asScalaSet
      val keys = asScalaSet( config.entrySet ) map ( _.getKey ) map ( s => s.indexOf('.') match {
        case -1 => s
        case i => s.substring(0,i)
      })
      keys.toSeq
    }
  }

}
