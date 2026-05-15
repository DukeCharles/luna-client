/**
 * Represents the base transformation skeleton for an animation.
 * It maps specific transformation opcodes (rotations, translations, etc.)
 * to groups of bones/vertices.
 */
public class Skeleton {

	public int instructionCount;
	public int[] opcodes;
	public int[][] boneGroups;

	public Skeleton(JagBuffer buffer, int dummy) {
		instructionCount = buffer.getByte();
		if (dummy != 0)
			throw new NullPointerException();

		opcodes = new int[instructionCount];
		boneGroups = new int[instructionCount][];

		for (int i = 0; i < instructionCount; i++)
			opcodes[i] = buffer.getByte();

		for (int i = 0; i < instructionCount; i++) {
			int groupSize = buffer.getByte();
			boneGroups[i] = new int[groupSize];
			for (int j = 0; j < groupSize; j++) {
				boneGroups[i][j] = buffer.getByte();
			}
		}
	}

}
