package by.protest.bot.liquibase;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.context.ConfigurableApplicationContext;

public class ScriptRunner {
	// FIXME move all the liq-classes here
	private static final String RESOURCE_PATH = "/WEB-INF/classes/static/database/";
/**
	public static void runScripts(ConfigurableApplicationContext context, String... files) {
		LiquibaseRunner lRunner = new LiquibaseRunner();
		for(String file:files) {
				InputStream f2;
				try {
					f2 = context.getResource(RESOURCE_PATH+file).getInputStream();
					lRunner.applyResourseStream(f2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
}

		lRunner.applyLogin("webadmin");
		lRunner.applyPassword("MYZkds77488");
		lRunner.applyUrl("jdbc:postgresql://10.100.2.55:5432/db_bot");
		
		lRunner.applyExequtionLevel(LiquibaseDefaultVariables.PRPODUCTION_REQUIRENMENT_LEVEL);
		lRunner.applyExequtionLevel(LiquibaseDefaultVariables.DEVELOPMENT_REQUIRENMENT_LEVEL);
		lRunner.applyExequtionLevel(LiquibaseDefaultVariables.TEST_REQUIRENMENT_LEVEL);
		lRunner.init();
		lRunner.runScripts();
	}

*/
}
