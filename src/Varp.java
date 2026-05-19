/**
 * VarpDefinition (Variable Player) represents a player-specific variable
 * used to track state between the client and the server.
 *
 * Varps are 32-bit integers stored in an array (historically size 4000).
 * They are used for:
 * 1. Configs: Transforming objects, NPCs, or hitmarks (e.g., farming patches).
 * 2. Client Scripts: Toggling interface buttons.
 * 3. Direct Code: Triggering internal client logic like volume or brightness.
 *
 * Varbits are bit-packed subsets of these Varps.
 */
public class Varp {

	public Varp() {
		aBoolean710 = false;
		aBoolean711 = true;
		aBoolean713 = false;
		aBoolean716 = false;
		anInt717 = -1;
		aBoolean718 = true;
	}

	public static int totalCount;
	public static Varp[] varpTable;
	public static int clientCodeCount;
	public static int[] anIntArray706;
	public String aString707;
	public int anInt708;
	public int anInt709;
	public boolean aBoolean710;
	public boolean aBoolean711;
	public int type;
	public boolean aBoolean713;
	public int anInt714;
	public int anInt715;
	public boolean aBoolean716;
	public int anInt717;
	public boolean aBoolean718;

	/**
	 * Unpacks the varp.dat archive and populates the definitions table.
	 */
	public static void unpack(Archive archive) {
		JagBuffer buf = new JagBuffer(archive.get("varp.dat"));
		clientCodeCount = 0;
		totalCount = buf.getShort();

		if (varpTable == null)
			varpTable = new Varp[totalCount];

		if (anIntArray706 == null)
			anIntArray706 = new int[totalCount];

		for (int j = 0; j < totalCount; j++) {
			if (varpTable[j] == null)
				varpTable[j] = new Varp();
			varpTable[j].init(j, buf);
		}

		if (buf.position != buf.buffer.length)
			System.out.println("varptype load mismatch");
	}

	/**
	 * Parses attributes from the data buffer.
	 */
	public void init(int id, JagBuffer buf) {
		do {
			int attribute = buf.getByte();

			if (attribute == 0)
				return;
			if (attribute == 1)
				anInt708 = buf.getByte();
			else if (attribute == 2)
				anInt709 = buf.getByte();
			else if (attribute == 3) {
				aBoolean710 = true;
				anIntArray706[clientCodeCount++] = id;
			} else if (attribute == 4)
				aBoolean711 = false;
			else if (attribute == 5)
				type = buf.getShort();
			else if (attribute == 6)
				aBoolean713 = true;
			else if (attribute == 7)
				anInt714 = buf.getInt();
			else if (attribute == 8) {
				anInt715 = 1;
				aBoolean716 = true;
			} else if (attribute == 10)
				aString707 = buf.getString();
			else if (attribute == 11)
				aBoolean716 = true;
			else if (attribute == 12)
				anInt717 = buf.getInt();
			else if (attribute == 13) {
				anInt715 = 2;
				aBoolean716 = true;
			} else if (attribute == 14)
				aBoolean718 = false;
			else
				System.out.println("Error unrecognised config code: " + attribute);
		} while (true);
	}
}
