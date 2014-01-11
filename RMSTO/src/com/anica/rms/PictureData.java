package com.anica.rms;

public class PictureData {

	public PictureData() {

	}

	@com.google.gson.annotations.SerializedName("id")
	public int KEY_ROWID;

	@com.google.gson.annotations.SerializedName("docnum")
	public String KEY_DOCNUM = "docnum";

	@com.google.gson.annotations.SerializedName("itemnum")
	public String KEY_INUM = "itemnum";

	@com.google.gson.annotations.SerializedName("seqnum")
	public String SEQ_NUM = "seqnum";

	@com.google.gson.annotations.SerializedName("createdate")
	public String KEY_CREATEDATE = "createdate";

	@com.google.gson.annotations.SerializedName("hstatus")
	public String KEY_HSTATUS = "hstatus";

	@com.google.gson.annotations.SerializedName("objnum")
	public String KEY_OBJNUM = "objnum";

	@com.google.gson.annotations.SerializedName("remarks")
	public String KEY_REMARKS = "remarks";

	@com.google.gson.annotations.SerializedName("location")
	public String KEY_LOCATION = "location";

	@com.google.gson.annotations.SerializedName("user")
	public String KEY_USER = "user";

	@com.google.gson.annotations.SerializedName("filepath")
	public String KEY_FILEPATH = "filepath";

	@com.google.gson.annotations.SerializedName("imagedata")
	public String KEY_IMAGEDATTA = "imagedata";

	@Override
	public boolean equals(Object o) {
		return o instanceof PictureData
				&& ((PictureData) o).KEY_ROWID == KEY_ROWID;
	}

	@com.google.gson.annotations.SerializedName("channel")
	private String mRegistrationId;

	public String getRegistrationId() {
		return mRegistrationId;
	}

	public final void setRegistrationId(String registrationId) {
		mRegistrationId = registrationId;
	}

}
