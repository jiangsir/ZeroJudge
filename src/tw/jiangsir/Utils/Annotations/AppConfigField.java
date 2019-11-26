package tw.jiangsir.Utils.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import tw.zerojudge.Configs.AppConfig;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AppConfigField {
	AppConfig.FIELD name();
}
