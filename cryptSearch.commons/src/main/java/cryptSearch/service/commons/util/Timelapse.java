package cryptSearch.service.commons.util;

import java.util.Date;

public class Timelapse {
	public Timelapse() {
		super();
		last = new Date();
	}
	private Date last;
	public void print(String msg ){
		Date now = new Date();
		System.out.println("Tempo: "+(now.getTime()-last.getTime())+" ms \t- "+msg);
		last = now;
	}
}
