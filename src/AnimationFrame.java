// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public class AnimationFrame {

	public AnimationFrame() {
	}

	public static int dummyValue = 217;
	public static boolean dummyBoolean;
	public static AnimationFrame[] animationFrames;
	public int frameDuration;
	public Skeleton skeleton;
	public int instructionCount;
	public int[] instructionIndices;
	public int[] transformationX;
	public int[] transformationY;
	public int[] transformationZ;
	public static boolean[] hasTransparency;

	public static void initFrames(int maxAnimationId) {
		animationFrames = new AnimationFrame[maxAnimationId + 1];
		hasTransparency = new boolean[maxAnimationId + 1];
		for (int frameId = 0; frameId < maxAnimationId + 1; frameId++)
			hasTransparency[frameId] = true;

	}

	public static void unpackFrames(byte[] animationData, boolean shouldProcess) {
		JagBuffer headerBuffer = new JagBuffer(animationData);
		headerBuffer.position = animationData.length - 8;
		int frameDataLength = headerBuffer.getShort();
		int transformDataLength = headerBuffer.getShort();
		int offsetDataLength = headerBuffer.getShort();
		if (!shouldProcess)
			return;
		int skeletonDataLength = headerBuffer.getShort();
		int currentOffset = 0;
		JagBuffer frameBuffer = new JagBuffer(animationData);
		frameBuffer.position = currentOffset;
		currentOffset += frameDataLength + 2;
		JagBuffer transformBuffer = new JagBuffer(animationData);
		transformBuffer.position = currentOffset;
		currentOffset += transformDataLength;
		JagBuffer offsetBuffer = new JagBuffer(animationData);
		offsetBuffer.position = currentOffset;
		currentOffset += offsetDataLength;
		JagBuffer frameTypeBuffer = new JagBuffer(animationData);
		frameTypeBuffer.position = currentOffset;
		currentOffset += skeletonDataLength;
		JagBuffer skeletonBuffer = new JagBuffer(animationData);
		skeletonBuffer.position = currentOffset;
		Skeleton animationSkeleton = new Skeleton(skeletonBuffer, 0);
		int numFramesToLoad = frameBuffer.getShort();
		int[] instructionIndices = new int[500];
		int[] transformationX = new int[500];
		int[] transformationY = new int[500];
		int[] transformationZ = new int[500];
		for (int frameIndex = 0; frameIndex < numFramesToLoad; frameIndex++) {
			int frameId = frameBuffer.getShort();
			AnimationFrame frame = animationFrames[frameId] = new AnimationFrame();
			frame.frameDuration = frameTypeBuffer.getByte();
			frame.skeleton = animationSkeleton;
			int numInstructions = frameBuffer.getByte();
			int lastInstructionIndex = -1;
			int currentInstructionCount = 0;
			for (int instructionLoopIndex = 0; instructionLoopIndex < numInstructions; instructionLoopIndex++) {
				int instructionFlags = transformBuffer.getByte();
				if (instructionFlags > 0) {
					if (animationSkeleton.opcodes[instructionLoopIndex] != 0) {
						for (int prevInstructionIndex = instructionLoopIndex - 1; prevInstructionIndex > lastInstructionIndex; prevInstructionIndex--) {
							if (animationSkeleton.opcodes[prevInstructionIndex] != 0)
								continue;
							instructionIndices[currentInstructionCount] = prevInstructionIndex;
							transformationX[currentInstructionCount] = 0;
							transformationX[currentInstructionCount] = 0;
							transformationY[currentInstructionCount] = 0;
							transformationZ[currentInstructionCount] = 0;
							currentInstructionCount++;
							break;
						}

					}
					instructionIndices[currentInstructionCount] = instructionLoopIndex;
					char defaultOffset = '\0';
					if (animationSkeleton.opcodes[instructionLoopIndex] == 3)
						defaultOffset = '\200';
					if ((instructionFlags & 1) != 0)
						transformationX[currentInstructionCount] = offsetBuffer.getSignedSmart();
					else
						transformationX[currentInstructionCount] = defaultOffset;
					if ((instructionFlags & 2) != 0)
						transformationY[currentInstructionCount] = offsetBuffer.getSignedSmart();
					else
						transformationY[currentInstructionCount] = defaultOffset;
					if ((instructionFlags & 4) != 0)
						transformationZ[currentInstructionCount] = offsetBuffer.getSignedSmart();
					else
						transformationZ[currentInstructionCount] = defaultOffset;
					lastInstructionIndex = instructionLoopIndex;
					currentInstructionCount++;
					if (animationSkeleton.opcodes[instructionLoopIndex] == 5)
						hasTransparency[frameId] = false;
				}
			}

			frame.instructionCount = currentInstructionCount;
			frame.instructionIndices = new int[currentInstructionCount];
			frame.transformationX = new int[currentInstructionCount];
			frame.transformationY = new int[currentInstructionCount];
			frame.transformationZ = new int[currentInstructionCount];
			for (int i = 0; i < currentInstructionCount; i++) {
				frame.instructionIndices[i] = instructionIndices[i];
				frame.transformationX[i] = transformationX[i];
				frame.transformationY[i] = transformationY[i];
				frame.transformationZ[i] = transformationZ[i];
			}

		}

	}

	public static void clearFrames(boolean shouldClear) {
		if (shouldClear)
			dummyValue = 189;
		animationFrames = null;
	}

	public static AnimationFrame forId(int frameId) {
		if (animationFrames == null)
			return null;
		else
			return animationFrames[frameId];
	}

	public static boolean isFrameTransparent(int frameId) {
		return frameId == -1;
	}



}
