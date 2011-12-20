package org.pokepal101.log.pokepal101.log;

public class Position implements java.io.Serializable
{
    static final long serialVersionUID = -6953548090074503L;
    private int x, y, z;

    public Position (int x1, int y1, int z1)
    {
        x = x1;
        y = y1;
        z = z1;
    }

    public int getX ()
    {
        return x;
    }

    public int getY ()
    {
        return x;
    }

    public int getZ ()
    {
        return x;
    }

    public boolean equals (Object oth)
    {
        if (!(oth instanceof Position))
            return false;
        return (oth.hashCode() == hashCode());
    }

    public int hashCode ()
    {
        return toString().hashCode();
    }

    public String toString ()
    {
        return x + ", " + y + ", " + z;
    }

	public String getData ()
	{
		return x + ";" + y + ";" + z;
	}
}