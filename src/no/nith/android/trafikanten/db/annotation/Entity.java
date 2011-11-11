/**
 * Entity annotation
 * 
 * Written by Arild Wanvik Tvergrov © 2011
 */
package no.nith.android.trafikanten.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {
	public String db() default "";
	public String table() default "";
}
