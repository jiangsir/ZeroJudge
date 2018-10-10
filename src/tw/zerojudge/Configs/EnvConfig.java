package tw.zerojudge.Configs;

public class EnvConfig extends AppConfig {
	public EnvConfig() {

	}



	public long getUsedMemory() {
		Runtime runtime = Runtime.getRuntime();
		return runtime.totalMemory() - runtime.freeMemory();
	}

	public long getMaxMemory() {
		Runtime runtime = Runtime.getRuntime();
		return runtime.maxMemory();
	}

	public long getTotalMemory() {
		Runtime runtime = Runtime.getRuntime();
		return runtime.totalMemory();
	}

}
