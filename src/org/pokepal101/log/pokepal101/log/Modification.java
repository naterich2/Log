package org.pokepal101.log.pokepal101.log;

public class Modification implements java.io.Serializable
{
    static final long serialVersionUID = 6797996890572426758L;
    private String who;
    private boolean placed;
    private String blockid = "?";
    private String date = "?/?/? ?:??";

    public Modification (String w, boolean p, String b, String d)
    {
        who = w;
        placed = p;
        blockid = b;
        date = d;
    }

    public String getWho ()
    {
        return who;
    }

    public boolean getPlaced ()
    {
        return placed;
    }

    public String getBlockID ()
    {
        return blockid;
    }

    public String getDate ()
    {
        return date;
    }

	public String getData ()
	{
		return who + ";" + placed + ";" + blockid + ";" + date;
	}
}