package com.jagex.runescape;// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

/**
 * Represents a single animation frame for skeletal animation in the com.jagex.runescape.Model system.
 *
 * <p>Each com.jagex.runescape.AnimationFrame contains a set of transformation instructions to be applied
 * to bone groups within a model. The frame stores references to the associated skeleton,
 * a list of bone group indices that have transformations, and the transformation values
 * (translation, rotation, or scaling) for each instruction.</p>
 *
 * <p>com.jagex.runescape.Animation frames are loaded in bulk from the cache and pooled globally for efficient
 * reuse across multiple animated entities.</p>
 */
public class AnimationFrame {

	/**
	 * Constructs a new com.jagex.runescape.AnimationFrame instance with default state.
	 *
	 * <p>This is typically called during deserialization from cache data.</p>
	 */
	public AnimationFrame() {
	}

	/**
	 * Placeholder/dummy static value used for internal state management.
	 */
	public static int dummyValue = 217;
	/**
	 * Placeholder/dummy static boolean flag.
	 */
	public static boolean dummyBoolean;
	/**
	 * Global array containing all loaded animation frames, indexed by frame ID.
	 * This pool is initialized with {@link #initFrames(int)} and populated by {@link #unpackFrames(byte[], boolean)}.
	 */
	public static AnimationFrame[] animationFrames;
	/**
	 * Duration of this animation frame in game ticks.
	 * Determines how long this frame is displayed during playback.
	 */
	public int frameDuration;
	/**
	 * Reference to the skeleton structure that defines the bone hierarchy and com.jagex.runescape.opcodes
	 * for this frame. Shared across all frames of the same animation sequence.
	 */
	public Skeleton skeleton;
	/**
	 * Number of transformation instructions in this frame.
	 */
	public int instructionCount;
	/**
	 * Array of bone/group indices indicating which bones have transformations in this frame.
	 * Used to efficiently apply only the necessary transformations without scanning all bones.
	 */
	public int[] instructionIndices;
	/**
	 * X-axis transformation values corresponding to each instruction.
	 * Interpretation depends on the opcode: translation delta, rotation angle, or scale factor.
	 */
	public int[] transformationX;
	/**
	 * Y-axis transformation values corresponding to each instruction.
	 * Interpretation depends on the opcode: translation delta, rotation angle, or scale factor.
	 */
	public int[] transformationY;
	/**
	 * Z-axis transformation values corresponding to each instruction.
	 * Interpretation depends on the opcode: translation delta, rotation angle, or scale factor.
	 */
	public int[] transformationZ;
	/**
	 * Global array tracking which frames contain transparency modifications (opcode 5).
	 * Indexed by frame ID; false indicates the frame does not modify face transparency.
	 */
	public static boolean[] hasTransparency;

	/**
	 * Initializes the global animation frame system.
	 *
	 * <p>This method allocates the global {@link #animationFrames} pool and initializes
	 * the {@link #hasTransparency} tracking array. All animation frames will be loaded
	 * later via {@link #unpackFrames(byte[], boolean)}.</p>
	 *
	 * @param maxAnimationId The maximum frame ID that will be used (array size = maxAnimationId + 1).
	 */
	public static void initFrames(int maxAnimationId) {
		animationFrames = new AnimationFrame[maxAnimationId + 1];
		hasTransparency = new boolean[maxAnimationId + 1];
		for (int frameId = 0; frameId < maxAnimationId + 1; frameId++)
			hasTransparency[frameId] = true;

	}

	/**
	 * Deserializes animation frame data from a raw byte array and populates the global frame pool.
	 *
	 * <p>This method performs multiple deserialization passes to reconstruct animation frames:
	 * <ul>
	 *   <li><b>Header Parsing:</b> Reads frame data length, transformation data length, and skeleton data length from the file header.</li>
	 *   <li><b>Buffer Positioning:</b> Creates separate com.jagex.runescape.JagBuffer instances pointing at different sections of the raw data.</li>
	 *   <li><b>Frame Iteration:</b> For each frame, reads frame ID, duration, and instruction count.</li>
	 *   <li><b>Instruction Parsing:</b> Reads transformation flags and values for each bone instruction, handling default values.</li>
	 *   <li><b>Array Sizing:</b> Allocates appropriately-sized arrays for each frame's instruction data.</li>
	 * </ul></p>
	 *
	 * @param animationData   The raw byte array containing all animation frame data, headers, and skeleton definitions.
	 * @param shouldProcess   If false, only reads and validates the header without processing frames. Allows lazy loading.
	 */
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

	/**
	 * Clears the global animation frame pool to assist garbage collection.
	 *
	 * <p>This method nullifies the global {@link #animationFrames} array, allowing the
	 * previously loaded frames to be reclaimed by the garbage collector. If animation
	 * data is needed again, it must be reloaded via {@link #unpackFrames(byte[], boolean)}.</p>
	 *
	 * @param shouldClear If true, additionally sets {@link #dummyValue} to 189 for internal state management.
	 */
	public static void clearFrames(boolean shouldClear) {
		if (shouldClear)
			dummyValue = 189;
		animationFrames = null;
	}

	/**
	 * Retrieves an animation frame by its ID.
	 *
	 * <p>If the frame pool has not been initialized or if the requested frame has not been
	 * loaded, this method returns null. The caller should check for null before using the result.</p>
	 *
	 * @param frameId The unique identifier of the animation frame to retrieve.
	 * @return The com.jagex.runescape.AnimationFrame instance if loaded; otherwise null.
	 */
	public static AnimationFrame forId(int frameId) {
		if (animationFrames == null)
			return null;
		else
			return animationFrames[frameId];
	}

	/**
	 * Checks if an animation frame ID represents a "no animation" state.
	 *
	 * <p>A frame ID of -1 is used as a sentinel value to indicate that no animation should be applied.</p>
	 *
	 * @param frameId The frame ID to check.
	 * @return True if frameId is -1 (no animation); otherwise false.
	 */
	public static boolean isFrameTransparent(int frameId) {
		return frameId == -1;
	}



}
