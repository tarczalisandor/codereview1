package it.usi.xframe.xas.servlet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;

public class Chronometer
{
	private ArrayList history = null;
	
	private class Partial {
		public Partial (long time, String message){
			this.time = time;
			this.message = message;
		}
		public long time = 0;
		public String message = null;
	}
	
	public Chronometer () {
		history = new ArrayList();
	}
	
	public Chronometer (boolean start) {
		history = new ArrayList();
		if(start==true)
			this.setStart(System.currentTimeMillis(), "start");
	}

	public Chronometer (boolean start, String message) {
		history = new ArrayList();
		if(start==true)
			this.setStart(System.currentTimeMillis(), message);
	}

	/**
	 * Clear the data from the chronometer
	 */
	public void Clear () {
		history.clear();
	}

	/**
	 * Start the chronometer
	 */
	public void start()
	{
		this.setStart(System.currentTimeMillis(), "start");
	}
	
	/**
	 * Stop the chronometer
	 */
	public void stop()
	{
		this.setEnd(System.currentTimeMillis(), "stop");

	}

	public void partial(String message)
	{
		this.setPartial(System.currentTimeMillis(), message);
	}

	public void setStart(long time, String message)
	{
		history.clear();
		Partial p = new Partial(time, message);
		history.add(p);
	}

	public void setPartial(long time, String message)
	{
		Partial p = new Partial(time, message);
		history.add(p);
	}

	public void setEnd(long time, String message)
	{
		Partial p = new Partial(time, message);
		history.add(p);
	}

	public long getStart()
	{
		Partial p = (Partial) history.get(0);
		return p.time;
	}

	public long getEnd()
	{
		Partial p = (Partial) history.get(history.size()-1);
		return p.time;
	}

	public long getElapsed()
	{
		long timeStart = this.getStart();
		long timeEnd = this.getEnd();
		return (timeEnd - timeStart);
	}

	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		String format = "dd-MM-yyyy HH:mm:ss.SSS";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date d = new Date();
		long timeStart = this.getStart();
		long timeEnd = this.getEnd();
		ListIterator li = history.listIterator();
	    int i=0;
		while(li.hasNext()){
	    	Partial p = (Partial) li.next();
			i++;
			timeEnd=p.time;
	    	ret.append(i+" time:"+p.time+" message:"+p.message+" elapsed:"+(timeEnd-timeStart)+"\n");
	    	timeStart=p.time;
	    }
		timeStart = this.getStart();
		timeEnd = this.getEnd();
		d.setTime(timeEnd - timeStart);
		ret.append("started: "+sdf.format(new Date(timeStart))+" elapsed: "+getElapsed()+" milliseconds");
		return ret.toString();
	}
}
