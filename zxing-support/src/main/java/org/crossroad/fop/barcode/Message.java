/**
 * 
 */
package org.crossroad.fop.barcode;

/**
 * @author e.soden
 *
 */
public class Message {
	public static enum HPOS {
		LEFT, CENTERED, RIGHT
	};

	public static enum VPOS {
		TOP, CENTERED, BOTTOM
	};

	private String text = null;
	private boolean show = false;

	private VPOS vpos = null;
	private HPOS hpos = null;

	/**
	 * 
	 */
	public Message() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the show
	 */
	public boolean isShow() {
		return show;
	}

	/**
	 * @param show the show to set
	 */
	public void setShow(boolean show) {
		this.show = show;
	}

	/**
	 * @return the vpos
	 */
	public VPOS getVpos() {
		return vpos;
	}

	/**
	 * @param vpos the vpos to set
	 */
	public void setVpos(VPOS vpos) {
		this.vpos = vpos;
	}

	/**
	 * @return the hpos
	 */
	public HPOS getHpos() {
		return hpos;
	}

	/**
	 * @param hpos the hpos to set
	 */
	public void setHpos(HPOS hpos) {
		this.hpos = hpos;
	}

	
}
