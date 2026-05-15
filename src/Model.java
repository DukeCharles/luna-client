/**
 * Represents a 3D mesh in the software rendering engine.
 *
 * <p>The Model class is the core of the 3D pipeline, responsible for storing vertex and face data,
 * performing spatial transformations (rotation, scaling, translation), and calculating lighting.
 * It utilizes a software-based rasterization approach, employing a "Painter's Algorithm"
 * with Z-sorting bins and priority layers to handle depth without a hardware Z-buffer.</p>
 *
 * <p>To maximize performance on legacy hardware, this class uses several optimizations:
 * <ul>
 *   <li>Static buffer pooling for projection and clipping to minimize Garbage Collection overhead.</li>
 *   <li>16-bit fixed-point arithmetic for trigonometric calculations and 3D projection.</li>
 *   <li>Parallel arrays for vertex and face attributes to improve cache locality.</li>
 * </ul></p>
 */
public class Model extends Entity {

	public Model(int id) {
		dummyVar = 932;
		dummVar2 = 426;
		aBoolean1638 = false;
		aBoolean1639 = true;
		anInt1640 = -252;
		aBoolean1641 = false;
		isPriorityPicking = false;
		if (id <= 0)
			anInt1640 = -110;
	}

	public Model(int i, int j) {
		dummyVar = 932;
		dummVar2 = 426;

		aBoolean1638 = false;
		aBoolean1639 = true;
		anInt1640 = -252;
		aBoolean1641 = false;
		isPriorityPicking = false;
		anInt1642++;

		ModelHeader modelHeader = Model.modelHeaders[i];
		verticesCount = modelHeader.anInt534;
		faceCount = modelHeader.anInt535;
		textureVertexCount = modelHeader.anInt536;
		verticesX = new int[verticesCount];
		verticesY = new int[verticesCount];
		verticesZ = new int[verticesCount];
		faceIndicesX = new int[faceCount];
		faceIndicesY = new int[faceCount];
		faceIndicesZ = new int[faceCount];
		textureVertexIndicesA = new int[textureVertexCount];
		textureVertexIndicesB = new int[textureVertexCount];
		textureVertexIndicesC = new int[textureVertexCount];
		if (modelHeader.anInt541 >= 0)
			vertexBoneIds = new int[verticesCount];
		if (modelHeader.anInt545 >= 0)
			faceRenderTypes = new int[faceCount];
		if (modelHeader.anInt546 >= 0)
			facePriorities = new int[faceCount];
		else
			defaultPriority = -modelHeader.anInt546 - 1;
		if (modelHeader.anInt547 >= 0)
			faceTransparency = new int[faceCount];
		if (modelHeader.anInt548 >= 0)
			faceBoneIds = new int[faceCount];
		colors = new int[faceCount];
		JagBuffer class50_sub1_sub2 = new JagBuffer(modelHeader.aByteArray533);
		class50_sub1_sub2.position = modelHeader.anInt537;
		JagBuffer class50_sub1_sub2_1 = new JagBuffer(modelHeader.aByteArray533);
		class50_sub1_sub2_1.position = modelHeader.anInt538;
		JagBuffer class50_sub1_sub2_2 = new JagBuffer(modelHeader.aByteArray533);
		class50_sub1_sub2_2.position = modelHeader.anInt539;


		if (j >= 0)
			aBoolean1641 = !aBoolean1641;
		JagBuffer class50_sub1_sub2_3 = new JagBuffer(modelHeader.aByteArray533);
		class50_sub1_sub2_3.position = modelHeader.anInt540;
		JagBuffer class50_sub1_sub2_4 = new JagBuffer(modelHeader.aByteArray533);
		class50_sub1_sub2_4.position = modelHeader.anInt541;
		int k = 0;
		int l = 0;
		int i1 = 0;
		for (int j1 = 0; j1 < verticesCount; j1++) {
			int k1 = class50_sub1_sub2.getByte();
			int i2 = 0;
			if ((k1 & 1) != 0)
				i2 = class50_sub1_sub2_1.getSignedSmart();
			int k2 = 0;
			if ((k1 & 2) != 0)
				k2 = class50_sub1_sub2_2.getSignedSmart();
			int i3 = 0;
			if ((k1 & 4) != 0)
				i3 = class50_sub1_sub2_3.getSignedSmart();
			verticesX[j1] = k + i2;
			verticesY[j1] = l + k2;
			verticesZ[j1] = i1 + i3;
			k = verticesX[j1];
			l = verticesY[j1];
			i1 = verticesZ[j1];
			if (vertexBoneIds != null)
				vertexBoneIds[j1] = class50_sub1_sub2_4.getByte();
		}

		class50_sub1_sub2.position = modelHeader.anInt544;
		class50_sub1_sub2_1.position = modelHeader.anInt545;
		class50_sub1_sub2_2.position = modelHeader.anInt546;
		class50_sub1_sub2_3.position = modelHeader.anInt547;
		class50_sub1_sub2_4.position = modelHeader.anInt548;
		for (int l1 = 0; l1 < faceCount; l1++) {
			colors[l1] = class50_sub1_sub2.getShort();
			if (faceRenderTypes != null)
				faceRenderTypes[l1] = class50_sub1_sub2_1.getByte();
			if (facePriorities != null)
				facePriorities[l1] = class50_sub1_sub2_2.getByte();
			if (faceTransparency != null)
				faceTransparency[l1] = class50_sub1_sub2_3.getByte();
			if (faceBoneIds != null)
				faceBoneIds[l1] = class50_sub1_sub2_4.getByte();
		}

		class50_sub1_sub2.position = modelHeader.anInt542;
		class50_sub1_sub2_1.position = modelHeader.anInt543;
		int j2 = 0;
		int l2 = 0;
		int j3 = 0;
		int k3 = 0;
		for (int l3 = 0; l3 < faceCount; l3++) {
			int i4 = class50_sub1_sub2_1.getByte();
			if (i4 == 1) {
				j2 = class50_sub1_sub2.getSignedSmart() + k3;
				k3 = j2;
				l2 = class50_sub1_sub2.getSignedSmart() + k3;
				k3 = l2;
				j3 = class50_sub1_sub2.getSignedSmart() + k3;
				k3 = j3;
				faceIndicesX[l3] = j2;
				faceIndicesY[l3] = l2;
				faceIndicesZ[l3] = j3;
			}
			if (i4 == 2) {
				l2 = j3;
				j3 = class50_sub1_sub2.getSignedSmart() + k3;
				k3 = j3;
				faceIndicesX[l3] = j2;
				faceIndicesY[l3] = l2;
				faceIndicesZ[l3] = j3;
			}
			if (i4 == 3) {
				j2 = j3;
				j3 = class50_sub1_sub2.getSignedSmart() + k3;
				k3 = j3;
				faceIndicesX[l3] = j2;
				faceIndicesY[l3] = l2;
				faceIndicesZ[l3] = j3;
			}
			if (i4 == 4) {
				int k4 = j2;
				j2 = l2;
				l2 = k4;
				j3 = class50_sub1_sub2.getSignedSmart() + k3;
				k3 = j3;
				faceIndicesX[l3] = j2;
				faceIndicesY[l3] = l2;
				faceIndicesZ[l3] = j3;
			}
		}

		class50_sub1_sub2.position = modelHeader.anInt549;
		for (int j4 = 0; j4 < textureVertexCount; j4++) {
			textureVertexIndicesA[j4] = class50_sub1_sub2.getShort();
			textureVertexIndicesB[j4] = class50_sub1_sub2.getShort();
			textureVertexIndicesC[j4] = class50_sub1_sub2.getShort();
		}

	}

	public Model(int i, Model subModels[]) {
		dummyVar = 932;
		dummVar2 = 426;
		aBoolean1638 = false;
		aBoolean1639 = true;
		anInt1640 = -252;
		aBoolean1641 = false;
		isPriorityPicking = false;
		anInt1642++;
		boolean flag = false;
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		verticesCount = 0;
		faceCount = 0;
		textureVertexCount = 0;
		defaultPriority = -1;
		for (int j = 0; j < i; j++) {
			Model class50_sub1_sub4_sub4 = subModels[j];
			if (class50_sub1_sub4_sub4 != null) {
				verticesCount += class50_sub1_sub4_sub4.verticesCount;
				faceCount += class50_sub1_sub4_sub4.faceCount;
				textureVertexCount += class50_sub1_sub4_sub4.textureVertexCount;
				flag |= class50_sub1_sub4_sub4.faceRenderTypes != null;
				if (class50_sub1_sub4_sub4.facePriorities != null) {
					flag1 = true;
				} else {
					if (defaultPriority == -1)
						defaultPriority = class50_sub1_sub4_sub4.defaultPriority;
					if (defaultPriority != class50_sub1_sub4_sub4.defaultPriority)
						flag1 = true;
				}
				flag2 |= class50_sub1_sub4_sub4.faceTransparency != null;
				flag3 |= class50_sub1_sub4_sub4.faceBoneIds != null;
			}
		}

		verticesX = new int[verticesCount];
		verticesY = new int[verticesCount];
		verticesZ = new int[verticesCount];
		vertexBoneIds = new int[verticesCount];
		faceIndicesX = new int[faceCount];
		faceIndicesY = new int[faceCount];
		faceIndicesZ = new int[faceCount];
		textureVertexIndicesA = new int[textureVertexCount];
		textureVertexIndicesB = new int[textureVertexCount];
		textureVertexIndicesC = new int[textureVertexCount];
		if (flag)
			faceRenderTypes = new int[faceCount];
		if (flag1)
			facePriorities = new int[faceCount];
		if (flag2)
			faceTransparency = new int[faceCount];
		if (flag3)
			faceBoneIds = new int[faceCount];
		colors = new int[faceCount];
		verticesCount = 0;
		faceCount = 0;
		textureVertexCount = 0;
		int k = 0;
		for (int l = 0; l < i; l++) {
			Model class50_sub1_sub4_sub4_1 = subModels[l];
			if (class50_sub1_sub4_sub4_1 != null) {
				for (int i1 = 0; i1 < class50_sub1_sub4_sub4_1.faceCount; i1++) {
					if (flag)
						if (class50_sub1_sub4_sub4_1.faceRenderTypes == null) {
							faceRenderTypes[faceCount] = 0;
						} else {
							int j1 = class50_sub1_sub4_sub4_1.faceRenderTypes[i1];
							if ((j1 & 2) == 2)
								j1 += k << 2;
							faceRenderTypes[faceCount] = j1;
						}
					if (flag1)
						if (class50_sub1_sub4_sub4_1.facePriorities == null)
							facePriorities[faceCount] = class50_sub1_sub4_sub4_1.defaultPriority;
						else
							facePriorities[faceCount] = class50_sub1_sub4_sub4_1.facePriorities[i1];
					if (flag2)
						if (class50_sub1_sub4_sub4_1.faceTransparency == null)
							faceTransparency[faceCount] = 0;
						else
							faceTransparency[faceCount] = class50_sub1_sub4_sub4_1.faceTransparency[i1];
					if (flag3 && class50_sub1_sub4_sub4_1.faceBoneIds != null)
						faceBoneIds[faceCount] = class50_sub1_sub4_sub4_1.faceBoneIds[i1];
					colors[faceCount] = class50_sub1_sub4_sub4_1.colors[i1];
					faceIndicesX[faceCount] = getOrMergeVertex(class50_sub1_sub4_sub4_1,
							class50_sub1_sub4_sub4_1.faceIndicesX[i1]);
					faceIndicesY[faceCount] = getOrMergeVertex(class50_sub1_sub4_sub4_1,
							class50_sub1_sub4_sub4_1.faceIndicesY[i1]);
					faceIndicesZ[faceCount] = getOrMergeVertex(class50_sub1_sub4_sub4_1,
							class50_sub1_sub4_sub4_1.faceIndicesZ[i1]);
					faceCount++;
				}

				for (int k1 = 0; k1 < class50_sub1_sub4_sub4_1.textureVertexCount; k1++) {
					textureVertexIndicesA[textureVertexCount] = getOrMergeVertex(class50_sub1_sub4_sub4_1,
							class50_sub1_sub4_sub4_1.textureVertexIndicesA[k1]);
					textureVertexIndicesB[textureVertexCount] = getOrMergeVertex(class50_sub1_sub4_sub4_1,
							class50_sub1_sub4_sub4_1.textureVertexIndicesB[k1]);
					textureVertexIndicesC[textureVertexCount] = getOrMergeVertex(class50_sub1_sub4_sub4_1,
							class50_sub1_sub4_sub4_1.textureVertexIndicesC[k1]);
					textureVertexCount++;
				}

				k += class50_sub1_sub4_sub4_1.textureVertexCount;
			}
		}

	}

	public Model(int i, boolean flag, int j, Model[] modelsTable) {
		dummyVar = 932;
		dummVar2 = 426;
		aBoolean1638 = false;
		aBoolean1639 = true;
		anInt1640 = -252;
		aBoolean1641 = false;
		isPriorityPicking = false;
		anInt1642++;
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		boolean flag4 = false;
		verticesCount = 0;
		faceCount = 0;
		textureVertexCount = 0;
		defaultPriority = -1;
		for (int k = 0; k < i; k++) {
			Model model = modelsTable[k];
			if (model != null) {
				verticesCount += model.verticesCount;
				faceCount += model.faceCount;
				textureVertexCount += model.textureVertexCount;
				flag1 |= model.faceRenderTypes != null;
				if (model.facePriorities != null) {
					flag2 = true;
				} else {
					if (defaultPriority == -1)
						defaultPriority = model.defaultPriority;
					if (defaultPriority != model.defaultPriority)
						flag2 = true;
				}
				flag3 |= model.faceTransparency != null;
				flag4 |= model.colors != null;
			}
		}

		verticesX = new int[verticesCount];
		verticesY = new int[verticesCount];
		verticesZ = new int[verticesCount];
		faceIndicesX = new int[faceCount];
		faceIndicesY = new int[faceCount];
		faceIndicesZ = new int[faceCount];
		faceColorsA = new int[faceCount];
		faceColorsB = new int[faceCount];
		faceColorsC = new int[faceCount];
		textureVertexIndicesA = new int[textureVertexCount];
		textureVertexIndicesB = new int[textureVertexCount];
		textureVertexIndicesC = new int[textureVertexCount];
		if (flag1)
			faceRenderTypes = new int[faceCount];
		if (flag2)
			facePriorities = new int[faceCount];
		if (flag3)
			faceTransparency = new int[faceCount];
		if (flag4)
			colors = new int[faceCount];
		verticesCount = 0;
		if (j != 0)
			throw new NullPointerException();
		faceCount = 0;
		textureVertexCount = 0;
		int l = 0;
		for (int i1 = 0; i1 < i; i1++) {
			Model class50_sub1_sub4_sub4_1 = modelsTable[i1];
			if (class50_sub1_sub4_sub4_1 != null) {
				int j1 = verticesCount;
				for (int k1 = 0; k1 < class50_sub1_sub4_sub4_1.verticesCount; k1++) {
					verticesX[verticesCount] = class50_sub1_sub4_sub4_1.verticesX[k1];
					verticesY[verticesCount] = class50_sub1_sub4_sub4_1.verticesY[k1];
					verticesZ[verticesCount] = class50_sub1_sub4_sub4_1.verticesZ[k1];
					verticesCount++;
				}

				for (int l1 = 0; l1 < class50_sub1_sub4_sub4_1.faceCount; l1++) {
					faceIndicesX[faceCount] = class50_sub1_sub4_sub4_1.faceIndicesX[l1] + j1;
					faceIndicesY[faceCount] = class50_sub1_sub4_sub4_1.faceIndicesY[l1] + j1;
					faceIndicesZ[faceCount] = class50_sub1_sub4_sub4_1.faceIndicesZ[l1] + j1;
					faceColorsA[faceCount] = class50_sub1_sub4_sub4_1.faceColorsA[l1];
					faceColorsB[faceCount] = class50_sub1_sub4_sub4_1.faceColorsB[l1];
					faceColorsC[faceCount] = class50_sub1_sub4_sub4_1.faceColorsC[l1];
					if (flag1)
						if (class50_sub1_sub4_sub4_1.faceRenderTypes == null) {
							faceRenderTypes[faceCount] = 0;
						} else {
							int i2 = class50_sub1_sub4_sub4_1.faceRenderTypes[l1];
							if ((i2 & 2) == 2)
								i2 += l << 2;
							faceRenderTypes[faceCount] = i2;
						}
					if (flag2)
						if (class50_sub1_sub4_sub4_1.facePriorities == null)
							facePriorities[faceCount] = class50_sub1_sub4_sub4_1.defaultPriority;
						else
							facePriorities[faceCount] = class50_sub1_sub4_sub4_1.facePriorities[l1];
					if (flag3)
						if (class50_sub1_sub4_sub4_1.faceTransparency == null)
							faceTransparency[faceCount] = 0;
						else
							faceTransparency[faceCount] = class50_sub1_sub4_sub4_1.faceTransparency[l1];
					if (flag4 && class50_sub1_sub4_sub4_1.colors != null)
						colors[faceCount] = class50_sub1_sub4_sub4_1.colors[l1];
					faceCount++;
				}

				for (int j2 = 0; j2 < class50_sub1_sub4_sub4_1.textureVertexCount; j2++) {
					textureVertexIndicesA[textureVertexCount] = class50_sub1_sub4_sub4_1.textureVertexIndicesA[j2] + j1;
					textureVertexIndicesB[textureVertexCount] = class50_sub1_sub4_sub4_1.textureVertexIndicesB[j2] + j1;
					textureVertexIndicesC[textureVertexCount] = class50_sub1_sub4_sub4_1.textureVertexIndicesC[j2] + j1;
					textureVertexCount++;
				}

				l += class50_sub1_sub4_sub4_1.textureVertexCount;
			}
		}

		calculateRadius();
	}

	public Model(boolean flag, boolean flag1, boolean flag2,
				 Model model, boolean flag3) {
		dummyVar = 932;
		dummVar2 = 426;
		aBoolean1638 = false;
		aBoolean1639 = true;
		anInt1640 = -252;
		aBoolean1641 = false;
		isPriorityPicking = false;
		anInt1642++;
		verticesCount = model.verticesCount;
		faceCount = model.faceCount;
		textureVertexCount = model.textureVertexCount;


		if (flag1)
			anInt1640 = 498;
		if (flag) {
			verticesX = model.verticesX;
			verticesY = model.verticesY;
			verticesZ = model.verticesZ;
		} else {
			verticesX = new int[verticesCount];
			verticesY = new int[verticesCount];
			verticesZ = new int[verticesCount];
			for (int i = 0; i < verticesCount; i++) {
				verticesX[i] = model.verticesX[i];
				verticesY[i] = model.verticesY[i];
				verticesZ[i] = model.verticesZ[i];
			}

		}
		if (flag2) {
			colors = model.colors;
		} else {
			colors = new int[faceCount];
			for (int j = 0; j < faceCount; j++)
				colors[j] = model.colors[j];

		}
		if (flag3) {
			faceTransparency = model.faceTransparency;
		} else {
			faceTransparency = new int[faceCount];
			if (model.faceTransparency == null) {
				for (int k = 0; k < faceCount; k++)
					faceTransparency[k] = 0;

			} else {
				for (int l = 0; l < faceCount; l++)
					faceTransparency[l] = model.faceTransparency[l];

			}
		}
		vertexBoneIds = model.vertexBoneIds;
		faceBoneIds = model.faceBoneIds;
		faceRenderTypes = model.faceRenderTypes;
		faceIndicesX = model.faceIndicesX;
		faceIndicesY = model.faceIndicesY;
		faceIndicesZ = model.faceIndicesZ;
		facePriorities = model.facePriorities;
		defaultPriority = model.defaultPriority;
		textureVertexIndicesA = model.textureVertexIndicesA;
		textureVertexIndicesB = model.textureVertexIndicesB;
		textureVertexIndicesC = model.textureVertexIndicesC;
	}

	public Model(boolean flag, boolean flag1, int i, Model model) {
		dummyVar = 932;
		dummVar2 = 426;
		aBoolean1638 = false;
		aBoolean1639 = true;
		anInt1640 = -252;
		aBoolean1641 = false;
		isPriorityPicking = false;
		anInt1642++;
		verticesCount = model.verticesCount;
		faceCount = model.faceCount;
		textureVertexCount = model.textureVertexCount;
		if (flag) {
			verticesY = new int[verticesCount];
			for (int j = 0; j < verticesCount; j++)
				verticesY[j] = model.verticesY[j];

		} else {
			verticesY = model.verticesY;
		}
		if (flag1) {
			faceColorsA = new int[faceCount];
			faceColorsB = new int[faceCount];
			faceColorsC = new int[faceCount];
			for (int k = 0; k < faceCount; k++) {
				faceColorsA[k] = model.faceColorsA[k];
				faceColorsB[k] = model.faceColorsB[k];
				faceColorsC[k] = model.faceColorsC[k];
			}

			faceRenderTypes = new int[faceCount];
			if (model.faceRenderTypes == null) {
				for (int l = 0; l < faceCount; l++)
					faceRenderTypes[l] = 0;

			} else {
				for (int i1 = 0; i1 < faceCount; i1++)
					faceRenderTypes[i1] = model.faceRenderTypes[i1];

			}
			super.normals = new VertexNormal[verticesCount];
			for (int j1 = 0; j1 < verticesCount; j1++) {
				VertexNormal class40 = super.normals[j1] = new VertexNormal();
				VertexNormal class40_1 = ((Entity) (model)).normals[j1];
				class40.x = class40_1.x;
				class40.y = class40_1.y;
				class40.z = class40_1.z;
				class40.magnitude = class40_1.magnitude;
			}

			vertexNormalsTable = model.vertexNormalsTable;
		} else {
			faceColorsA = model.faceColorsA;
			faceColorsB = model.faceColorsB;
			faceColorsC = model.faceColorsC;
			faceRenderTypes = model.faceRenderTypes;
		}
		verticesX = model.verticesX;
		verticesZ = model.verticesZ;
		if (i != 0)
			aBoolean1638 = !aBoolean1638;
		colors = model.colors;
		faceTransparency = model.faceTransparency;
		facePriorities = model.facePriorities;
		defaultPriority = model.defaultPriority;
		faceIndicesX = model.faceIndicesX;
		faceIndicesY = model.faceIndicesY;
		faceIndicesZ = model.faceIndicesZ;
		textureVertexIndicesA = model.textureVertexIndicesA;
		textureVertexIndicesB = model.textureVertexIndicesB;
		textureVertexIndicesC = model.textureVertexIndicesC;
		super.height = ((Entity) (model)).height;
		maxBottomExtent = model.maxBottomExtent;
		modelRadius = model.modelRadius;
		modelBoundingSphere = model.modelBoundingSphere;
		totalDepthSortingRange = model.totalDepthSortingRange;
		packedXBounds = model.packedXBounds;
		packedZBounds = model.packedZBounds;
		lightingParameters = model.lightingParameters;
	}

	/**
	 * The maximum value a vector component can have before its square
	 * risks overflowing a 32-bit integer during normalization.
	 * 8192^2 * 3 is approx 201M, well under the 2.1B integer limit.
	 */
	private static final int MAX_NORMAL_COMPONENT = 8192;
	private static final int MAX_TRANSFORMATION_GROUPS = 256;

	/**
	 * The standard unit length for normalized vectors in this engine.
	 */
	private static final int NORMAL_SCALING_FACTOR = 256;
	private static final int RENDER_TYPE_FLAT_SHADING = 0x1;

	public int dummyVar;
	public int dummVar2;
	public boolean aBoolean1638;
	public boolean aBoolean1639;
	public int anInt1640;
	public boolean aBoolean1641;
	public static int anInt1642;
	public static Model anotherModel = new Model(852);
	public static int[] staticVertexX = new int[2000];
	public static int[] staticVertexZ = new int[2000];
	public static int[] staticVertexY = new int[2000];
	public static int[] staticTransparency = new int[2000];
	public int verticesCount;

	// Vertices
	public int[] verticesX;
	public int[] verticesY;
	public int[] verticesZ;

	//Faces Indices
	public int[] faceIndicesX;
	public int[] faceIndicesY;
	public int[] faceIndicesZ;

	public int faceCount;

	public int[] faceColorsA;
	public int[] faceColorsB;
	public int[] faceColorsC;
	/**
	 * Bit 0 & 1 (& 3): Determines the Shading Type.
	 * 0: Gouraud (Smooth) Shading.
	 * 1: Flat Shading.
	 * 2 or 3: Textured/Mapping mode.
	 * Bit 2 (& 4): Often determines Color Behavior
	 */
	public int[] faceRenderTypes;
	public int[] facePriorities;
	public int[] faceTransparency;
	public int[] colors;
	public int defaultPriority;
	public int textureVertexCount;
	public int[] textureVertexIndicesA;
	public int[] textureVertexIndicesB;
	public int[] textureVertexIndicesC;
	public int lightingParameters;
	public int packedXBounds;
	public int packedZBounds;
	public int modelRadius;
	public int maxBottomExtent;
	public int totalDepthSortingRange;
	public int modelBoundingSphere;
	public int anInt1675;
	public int[] vertexBoneIds;
	public int[] faceBoneIds;
	public int[][] vertexIndicesByBone;
	public int[][] faceIndicesByBone;
	public boolean isPriorityPicking;
	public VertexNormal[] vertexNormalsTable;
	public static ModelHeader[] modelHeaders;
	public static ModelProvider modelProvider;
	public static boolean[] faceIsOffScreen = new boolean[4096];
	public static boolean[] faceNeedsClipping = new boolean[4096];
	public static int[] projectedX = new int[4096];
	public static int[] projectedY = new int[4096];
	public static int[] projectedZ = new int[4096];
	public static int[] cameraX = new int[4096];
	public static int[] cameraY = new int[4096];
	public static int[] cameraZ = new int[4096];
	public static int[] faceDepthCounts = new int[1500];
	public static int[][] faceDepthBins = new int[1500][512];
	public static int[] priorityCounts = new int[12];
	public static int[][] priorityBins = new int[12][2000];
	public static int[] priorityDepthX = new int[2000];
	public static int[] priorityDepthY = new int[2000];
	public static int[] priorityAverages = new int[12];
	public static int[] clippedProjectedX = new int[10];
	public static int[] clippedProjectedY = new int[10];
	public static int[] clippedVertexColors = new int[10];
	public static int transformationPivotX;
	public static int transformationPivotY;
	public static int transformationPivotZ;
	public static boolean isPickingEnabled;
	public static int mouseX;
	public static int mouseY;
	public static int hoveredCount;
	public static int[] hoveredModels = new int[1000];
	public static int[] sineTable;
	public static int[] cosineTable;
	public static int[] colorLookupTable;
	public static int[] reciprocalTable;

	static {
		sineTable = ThreeDimensionalCanvas.sineTable;
		cosineTable = ThreeDimensionalCanvas.cosineTable;
		colorLookupTable = ThreeDimensionalCanvas.hslToRgbTable;
		reciprocalTable = ThreeDimensionalCanvas.reciprocalTable;
	}

	/**
	 * Nullifies all global static buffers to assist the Garbage Collector in reclaiming memory.
	 *
	 * @param preserveMathTables If true, shared mathematical tables (sine, cosine, reciprocal)
	 *                           remain in memory for other engine components to use.
	 */
	public static void dispose(boolean preserveMathTables) {
		// Only clear the shared reciprocal table if explicitly requested
		if (!preserveMathTables) {
			reciprocalTable = null;;
		}

		modelHeaders = null;
		faceIsOffScreen = null;
		faceNeedsClipping = null;
		projectedX = null;
		projectedY = null;
		projectedZ = null;
		cameraX = null;
		cameraY = null;
		cameraZ = null;
		//Clear depth sorting (Z-buffer) bins
		faceDepthCounts = null;
		faceDepthBins = null;
		//Clear priority-based rendering pools
		priorityCounts = null;
		priorityBins = null;
		priorityDepthX = null;
		priorityDepthY = null;
		priorityAverages = null;

		//Clear global lookup tables
		sineTable = null;
		cosineTable = null;
		colorLookupTable = null;
	}

	/**
	 * Initializes the global model system.
	 *
	 * @param modelCount The total number of models present in the cache.
	 * @param provider   The provider responsible for loading/requesting model data.
	 */
	public static void init(int modelCount, ModelProvider provider) {
		modelHeaders = new ModelHeader[modelCount];
		modelProvider = provider;
	}

	public static void method575(byte[] abyte0, int i, byte byte0) {
		if (byte0 != 7)
			return;
		if (abyte0 == null) {
			ModelHeader modelHeader = Model.modelHeaders[i] = new ModelHeader();
			modelHeader.anInt534 = 0;
			modelHeader.anInt535 = 0;
			modelHeader.anInt536 = 0;
			return;
		}
		JagBuffer class50_sub1_sub2 = new JagBuffer(abyte0);
		class50_sub1_sub2.position = abyte0.length - 18;
		ModelHeader modelHeader_1 = modelHeaders[i] = new ModelHeader();
		modelHeader_1.aByteArray533 = abyte0;
		modelHeader_1.anInt534 = class50_sub1_sub2.getShort();
		modelHeader_1.anInt535 = class50_sub1_sub2.getShort();
		modelHeader_1.anInt536 = class50_sub1_sub2.getByte();
		int j = class50_sub1_sub2.getByte();
		int k = class50_sub1_sub2.getByte();
		int l = class50_sub1_sub2.getByte();
		int i1 = class50_sub1_sub2.getByte();
		int j1 = class50_sub1_sub2.getByte();
		int k1 = class50_sub1_sub2.getShort();
		int l1 = class50_sub1_sub2.getShort();
		int i2 = class50_sub1_sub2.getShort();
		int j2 = class50_sub1_sub2.getShort();
		int k2 = 0;
		modelHeader_1.anInt537 = k2;
		k2 += modelHeader_1.anInt534;
		modelHeader_1.anInt543 = k2;
		k2 += modelHeader_1.anInt535;
		modelHeader_1.anInt546 = k2;
		if (k == 255)
			k2 += modelHeader_1.anInt535;
		else
			modelHeader_1.anInt546 = -k - 1;
		modelHeader_1.anInt548 = k2;
		if (i1 == 1)
			k2 += modelHeader_1.anInt535;
		else
			modelHeader_1.anInt548 = -1;
		modelHeader_1.anInt545 = k2;
		if (j == 1)
			k2 += modelHeader_1.anInt535;
		else
			modelHeader_1.anInt545 = -1;
		modelHeader_1.anInt541 = k2;
		if (j1 == 1)
			k2 += modelHeader_1.anInt534;
		else
			modelHeader_1.anInt541 = -1;
		modelHeader_1.anInt547 = k2;
		if (l == 1)
			k2 += modelHeader_1.anInt535;
		else
			modelHeader_1.anInt547 = -1;
		modelHeader_1.anInt542 = k2;
		k2 += j2;
		modelHeader_1.anInt544 = k2;
		k2 += modelHeader_1.anInt535 * 2;
		modelHeader_1.anInt549 = k2;
		k2 += modelHeader_1.anInt536 * 6;
		modelHeader_1.anInt538 = k2;
		k2 += k1;
		modelHeader_1.anInt539 = k2;
		k2 += l1;
		modelHeader_1.anInt540 = k2;
		k2 += i2;
	}

	/**
	 * Unloads a model header from the global cache.
	 *
	 * <p>This removes the metadata associated with the model ID, allowing the
	 * memory to be reclaimed. If the model is needed again, it will be
	 * re-requested through the {@code ModelProvider}.</p>
	 *
	 * @param modelId The unique identifier of the model to unload.
	 */
	public static void unloadModelHeader(int modelId) {
			modelHeaders[modelId] = null;
	}

	/**
	 * Retrieves a model instance by its ID.
	 *
	 * <p>If the model's metadata is not yet loaded, this method will trigger
	 * an asynchronous request via the {@code modelProvider} and return null.</p>
	 *
	 * @param id The unique identifier of the model to load.
	 * @return A new {@code Model} instance if the header is available; otherwise null.
	 */
	public static Model forId(int id) {
		if (modelHeaders == null) {
			return null;
		}

		ModelHeader modelHeader = Model.modelHeaders[id];

		// Check if the model metadata (header) is present in the cache
		if (modelHeader == null) {
			modelProvider.requestModel(id);
			return null;
		}

		return new Model(id, -478);

	}

	public static boolean isDownloaded(int id) {
		if (modelHeaders == null)
			return false;
		ModelHeader modelHeader = Model.modelHeaders[id];
		if (modelHeader == null) {
			modelProvider.requestModel(id);
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Copies the geometry and metadata from a source model into this instance.
	 *
	 * <p>To optimize performance and minimize Garbage Collection, this method utilizes
	 * static buffer pooling for vertex coordinates and transparency. While vertex
	 * positions are deep-copied into these buffers, many other attributes (indices,
	 * colors, bones) are shallow-copied by reference.</p>
	 *
	 * @param shareTransparency If true, the transparency array is shared by reference;
	 *                          if false, it is copied into a static buffer.
	 * @param source            The source model to copy data from.
	 */
	public void copyFrom(boolean shareTransparency, Model source) {
		verticesCount = source.verticesCount;
		faceCount = source.faceCount;
		textureVertexCount = source.textureVertexCount;

		// --- Vertex Buffer Pooling ---
		// Ensure the static global buffers are large enough to hold the incoming data
		if (staticVertexX.length < verticesCount) {
			staticVertexX = new int[verticesCount + 100];
			staticVertexY = new int[verticesCount + 100];
			staticVertexZ = new int[verticesCount + 100];
		}

		// Assign this model's vertex pointers to the global static pool
		verticesX = staticVertexX;
		verticesY = staticVertexY;
		verticesZ = staticVertexZ;

		// Deep copy vertex coordinates so this model can be animated
		// independently of the source model.
		for (int j = 0; j < verticesCount; j++) {
			verticesX[j] = source.verticesX[j];
			verticesY[j] = source.verticesY[j];
			verticesZ[j] = source.verticesZ[j];
		}

		// --- Transparency Management ---
		if (shareTransparency) {
			faceTransparency = source.faceTransparency;
		} else {
			if (staticTransparency.length < faceCount)
				staticTransparency = new int[faceCount + 100];
			faceTransparency = staticTransparency;
			if (source.faceTransparency == null) {
				for (int f = 0; f < faceCount; f++)
					faceTransparency[f] = 0;

			} else {
				for(int f = 0; f < faceCount; f++)
					faceTransparency[f] = source.faceTransparency[f];

			}
		}

		// --- Shallow Copy (Shared References) ---
		// These values are generally not modified during animation,
		// so it is safe to share the reference.
		faceRenderTypes = source.faceRenderTypes;
		colors = source.colors;
		facePriorities = source.facePriorities;
		defaultPriority = source.defaultPriority;
		faceIndicesByBone = source.faceIndicesByBone;
		vertexIndicesByBone = source.vertexIndicesByBone;
		faceIndicesX = source.faceIndicesX;
		faceIndicesY = source.faceIndicesY;
		faceIndicesZ = source.faceIndicesZ;
		faceColorsA = source.faceColorsA;
		faceColorsB = source.faceColorsB;
		faceColorsC = source.faceColorsC;
		textureVertexIndicesA = source.textureVertexIndicesA;
		textureVertexIndicesB = source.textureVertexIndicesB;
		textureVertexIndicesC = source.textureVertexIndicesC;
	}

	/**
	 * Merges a vertex from a source model into the current model, preventing duplicates.
	 *
	 * <p>This method performs a linear search to see if a vertex with identical coordinates
	 * already exists in the current vertex pool. If found, it returns the existing index.
	 * Otherwise, it appends the vertex to the pool and returns the new index.</p>
	 *
	 * @param sourceModel       The model containing the vertex to be copied.
	 * @param sourceVertexIndex The index of the vertex in the source model.
	 * @return The index of the vertex within the current model.
	 */
	public int getOrMergeVertex(Model sourceModel, int sourceVertexIndex) {
		int existingIndex = -1;

		// Extract coordinates from the source model
		int sourceX = sourceModel.verticesX[sourceVertexIndex];
		int sourceY = sourceModel.verticesY[sourceVertexIndex];
		int sourceZ = sourceModel.verticesZ[sourceVertexIndex];

		// Search for an identical vertex in the current model
		for (int v = 0; v < verticesCount; v++) {
			if (sourceX != verticesX[v] || sourceY != verticesY[v] || sourceZ != verticesZ[v])
				continue;
			existingIndex = v;
			break;
		}

		// If the vertex was not found, append it to the current model's buffers
		if (existingIndex == -1) {
			verticesX[verticesCount] = sourceX;
			verticesY[verticesCount] = sourceY;
			verticesZ[verticesCount] = sourceZ;
			if (sourceModel.vertexBoneIds != null)
				vertexBoneIds[verticesCount] = sourceModel.vertexBoneIds[sourceVertexIndex];
			existingIndex = verticesCount++;
		}
		return existingIndex;
	}

	public void calculateRadius() {
		super.height = 0;
		modelRadius = 0;
		maxBottomExtent = 0;
		for (int j = 0; j < verticesCount; j++) {
			int k = verticesX[j];
			int l = verticesY[j];
			int i1 = verticesZ[j];
			if (-l > super.height)
				super.height = -l;
			if (l > maxBottomExtent)
				maxBottomExtent = l;
			int j1 = k * k + i1 * i1;
			if (j1 > modelRadius)
				modelRadius = j1;
		}

		modelRadius = (int) (Math.sqrt(modelRadius) + 0.98999999999999999D);
		modelBoundingSphere = (int) (Math.sqrt(modelRadius * modelRadius + super.height * super.height) + 0.98999999999999999D);
		totalDepthSortingRange = modelBoundingSphere + (int) (Math.sqrt(modelRadius * modelRadius + maxBottomExtent * maxBottomExtent) + 0.98999999999999999D);
	}

	public void updateVerticalBounds() {
		// Reset vertical extents
		super.height = 0; // Max distance above origin (negative Y)
		maxBottomExtent = 0; // Max distance below origin (positive Y)

		// Scan only the Y-axis
		for (int v = 0; v < verticesCount; v++) {
			int y = verticesY[v];
			if (-y > super.height)
				super.height = -y;
			if (y > maxBottomExtent)
				maxBottomExtent = y;
		}

		// Recalculate the 3D Bounding Sphere Radius
		// Using 0.99D before casting to (int) is a legacy "ceiling" trick to ensure the sphere
		// always fully encompasses the model vertices.
		modelBoundingSphere = (int) (Math.sqrt(modelRadius * modelRadius + super.height * super.height) + 0.99D);

		// Recalculate the total depth range for Z-sorting bins
		// This is (Distance to top-most point) + (Distance to bottom-most point)
		totalDepthSortingRange = modelBoundingSphere + (int) (Math.sqrt(modelRadius * modelRadius + maxBottomExtent * maxBottomExtent) + 0.99D);
	}

	/**
	 * Calculates the Axis-Aligned Bounding Box (AABB) and the bounding sphere radii for the model.
	 *
	 * <p>This method iterates through all vertices to find the minimum and maximum extents
	 * on all three axes. It also calculates the maximum horizontal distance from the origin
	 * to determine the model's circular and spherical bounds.</p>
	 */
	public void calculateAABB() {
		// Reset vertical extents
		super.height = 0; // Represents the maximum extent above the origin (negative Y)
		maxBottomExtent = 0; // Represents the maximum extent below the origin (positive Y)
		modelRadius = 0; // Square of the horizontal radius (X^2 + Z^2)


		int minX = Short.MAX_VALUE;
		int maxX = Short.MIN_VALUE;

		int minZ = Short.MAX_VALUE;
		int maxZ = Short.MIN_VALUE;

		for (int v = 0; v < verticesCount; v++) {
			int x = verticesX[v];
			int y = verticesY[v];
			int z = verticesZ[v];

			// Update Horizontal Bound Extremes
			if (x < minX) minX = x;
			if (x > maxX) maxX = x;
			if (z < minZ) minZ = z;
			if (z > maxZ) maxZ = z;

			// Update Vertical Extents
			// In this engine's coordinate system, negative Y is "up"
			if (-y > super.height) {
				super.height = -y;
			}
			if (y > maxBottomExtent) {
				maxBottomExtent = y;
			}

			// Track the squared horizontal distance from the origin
			int horizontalDistanceSq = x * x + z * z;
			if (horizontalDistanceSq > modelRadius) {
				modelRadius = horizontalDistanceSq;
			}
		}

		// Convert squared horizontal distance to actual radius
		modelRadius = (int) Math.sqrt(modelRadius);

		// anInt1674: The 3D radius from origin to the furthest top vertex
		modelBoundingSphere = (int) Math.sqrt(modelRadius * modelRadius + super.height * super.height);

		// The total depth range used for Z-buffer/Bin allocation
		// Calculated as the distance to the furthest top point + distance to furthest bottom point
		totalDepthSortingRange = modelBoundingSphere + (int) Math.sqrt(modelRadius * modelRadius + maxBottomExtent * maxBottomExtent);

		// Pack the AABB boundaries into 32-bit integers for high-performance culling checks
		// packedXBounds stores X-axis bounds (min in high bits, max in low bits)
		packedXBounds = (minX << 16) + (maxX & 0xffff);

		// packedZBounds stores Z-axis bounds (max in high bits, min in low bits)
		packedZBounds = (maxZ << 16) + (minZ & 0xffff);
	}

	/**
	 * Groups vertex and face indices by their respective transformation/bone IDs.
	 *
	 * <p>This method converts flat mapping arrays (vertexGroups and faceGroups) into
	 * structured 2D arrays (groupedVertexIndices and groupedFaceIndices). This
	 * allows the animation system to transform groups of geometry efficiently
	 * without scanning the entire model.</p>
	 */
	public void groupIndicesByTransform() {

		// Group Vertex Indices by Bone/Transformation ID
		if (vertexBoneIds != null) {
			int[] groupCounts = new int[MAX_TRANSFORMATION_GROUPS];
			int maxGroupId = 0;

			// Count occurrences of each group ID to determine sub-array sizes
			for (int v = 0; v < verticesCount; v++) {
				int groupId = vertexBoneIds[v];
				groupCounts[groupId]++;
				if (groupId > maxGroupId)
					maxGroupId = groupId;
			}

			// Allocate the outer 2D array based on the highest Group ID found
			vertexIndicesByBone = new int[maxGroupId + 1][];
			for (int g = 0; g <= maxGroupId; g++) {
				vertexIndicesByBone[g] = new int[groupCounts[g]];
				groupCounts[g] = 0;
			}

			for (int v = 0; v < verticesCount; v++) {
				int groupId = vertexBoneIds[v];
				vertexIndicesByBone[groupId][groupCounts[groupId]++] = v;
			}

			vertexBoneIds = null;
		}

		// Group Face Indices by Bone/Transformation ID
		if (faceBoneIds != null) {
			int[] groupCounts = new int[MAX_TRANSFORMATION_GROUPS];
			int maxGroupId = 0;

			// Count occurrences for faces
			for (int f = 0; f < faceCount; f++) {
				int groupId = faceBoneIds[f];
				groupCounts[groupId]++;
				if (groupId > maxGroupId)
					maxGroupId = groupId;
			}

			// Allocate grouped face index table
			faceIndicesByBone = new int[maxGroupId + 1][];
			for (int g = 0; g <= maxGroupId; g++) {
				faceIndicesByBone[g] = new int[groupCounts[g]];
				groupCounts[g] = 0;
			}

			// Populate face indices
			for (int f = 0; f < faceCount; f++) {
				int groupId = faceBoneIds[f];
				faceIndicesByBone[groupId][groupCounts[groupId]++] = f;
			}

			// Nullify the flat array to reclaim memory
			faceBoneIds = null;
		}
	}

	public void method585(int i, byte byte0) {
		if (vertexIndicesByBone == null)
			return;
		if (i == -1)
			return;
		AnimationFrame class21 = AnimationFrame.forId(i);
		if (class21 == null)
			return;
		Skeleton skeleton = class21.skeleton;
		if (byte0 == 6)
			byte0 = 0;
		else
			return;
		transformationPivotX = 0;
		transformationPivotY = 0;
		transformationPivotZ = 0;
		for (int j = 0; j < class21.instructionCount; j++) {
			int k = class21.instructionIndices[j];
			method587(skeleton.opcodes[k], skeleton.boneGroups[k], class21.transformationX[j],
					class21.transformationY[j], class21.transformationZ[j]);
		}

	}

	public void method586(int i, int j, int k, int[] ai) {
		if (k == -1)
			return;
		if (ai == null || i == -1) {
			method585(k, (byte) 6);
			return;
		}
		AnimationFrame class21 = AnimationFrame.forId(k);
		if (class21 == null)
			return;
		AnimationFrame class21_1 = AnimationFrame.forId(i);
		if (class21_1 == null) {
			method585(k, (byte) 6);
			return;
		}
		Skeleton skeleton = class21.skeleton;
		transformationPivotX = 0;
		if (j != 0)
			aBoolean1641 = !aBoolean1641;
		transformationPivotY = 0;
		transformationPivotZ = 0;
		int l = 0;
		int i1 = ai[l++];
		for (int j1 = 0; j1 < class21.instructionCount; j1++) {
			int k1;
			for (k1 = class21.instructionIndices[j1]; k1 > i1; i1 = ai[l++]);
			if (k1 != i1 || skeleton.opcodes[k1] == 0)
				method587(skeleton.opcodes[k1], skeleton.boneGroups[k1], class21.transformationX[j1],
						class21.transformationY[j1], class21.transformationZ[j1]);
		}

		transformationPivotX = 0;
		transformationPivotY = 0;
		transformationPivotZ = 0;
		l = 0;
		i1 = ai[l++];
		for (int l1 = 0; l1 < class21_1.instructionCount; l1++) {
			int i2;
			for (i2 = class21_1.instructionIndices[l1]; i2 > i1; i1 = ai[l++]);
			if (i2 == i1 || skeleton.opcodes[i2] == 0)
				method587(skeleton.opcodes[i2], skeleton.boneGroups[i2], class21_1.transformationX[l1],
						class21_1.transformationY[l1], class21_1.transformationZ[l1]);
		}

	}

	public void method587(int i, int ai[], int j, int k, int l) {
		int i1 = ai.length;
		if (i == 0) {
			int j1 = 0;
			transformationPivotX = 0;
			transformationPivotY = 0;
			transformationPivotZ = 0;
			for (int k2 = 0; k2 < i1; k2++) {
				int l3 = ai[k2];
				if (l3 < vertexIndicesByBone.length) {
					int ai5[] = vertexIndicesByBone[l3];
					for (int i5 = 0; i5 < ai5.length; i5++) {
						int j6 = ai5[i5];
						transformationPivotX += verticesX[j6];
						transformationPivotY += verticesY[j6];
						transformationPivotZ += verticesZ[j6];
						j1++;
					}

				}
			}

			if (j1 > 0) {
				transformationPivotX = transformationPivotX / j1 + j;
				transformationPivotY = transformationPivotY / j1 + k;
				transformationPivotZ = transformationPivotZ / j1 + l;
				return;
			} else {
				transformationPivotX = j;
				transformationPivotY = k;
				transformationPivotZ = l;
				return;
			}
		}
		if (i == 1) {
			for (int k1 = 0; k1 < i1; k1++) {
				int l2 = ai[k1];
				if (l2 < vertexIndicesByBone.length) {
					int ai1[] = vertexIndicesByBone[l2];
					for (int i4 = 0; i4 < ai1.length; i4++) {
						int j5 = ai1[i4];
						verticesX[j5] += j;
						verticesY[j5] += k;
						verticesZ[j5] += l;
					}

				}
			}

			return;
		}
		if (i == 2) {
			for (int l1 = 0; l1 < i1; l1++) {
				int i3 = ai[l1];
				if (i3 < vertexIndicesByBone.length) {
					int ai2[] = vertexIndicesByBone[i3];
					for (int j4 = 0; j4 < ai2.length; j4++) {
						int k5 = ai2[j4];
						verticesX[k5] -= transformationPivotX;
						verticesY[k5] -= transformationPivotY;
						verticesZ[k5] -= transformationPivotZ;
						int k6 = (j & 0xff) * 8;
						int l6 = (k & 0xff) * 8;
						int i7 = (l & 0xff) * 8;
						if (i7 != 0) {
							int j7 = sineTable[i7];
							int i8 = cosineTable[i7];
							int l8 = verticesY[k5] * j7 + verticesX[k5] * i8 >> 16;
							verticesY[k5] = verticesY[k5] * i8 - verticesX[k5] * j7 >> 16;
							verticesX[k5] = l8;
						}
						if (k6 != 0) {
							int k7 = sineTable[k6];
							int j8 = cosineTable[k6];
							int i9 = verticesY[k5] * j8 - verticesZ[k5] * k7 >> 16;
							verticesZ[k5] = verticesY[k5] * k7 + verticesZ[k5] * j8 >> 16;
							verticesY[k5] = i9;
						}
						if (l6 != 0) {
							int l7 = sineTable[l6];
							int k8 = cosineTable[l6];
							int j9 = verticesZ[k5] * l7 + verticesX[k5] * k8 >> 16;
							verticesZ[k5] = verticesZ[k5] * k8 - verticesX[k5] * l7 >> 16;
							verticesX[k5] = j9;
						}
						verticesX[k5] += transformationPivotX;
						verticesY[k5] += transformationPivotY;
						verticesZ[k5] += transformationPivotZ;
					}

				}
			}

			return;
		}
		if (i == 3) {
			for (int i2 = 0; i2 < i1; i2++) {
				int j3 = ai[i2];
				if (j3 < vertexIndicesByBone.length) {
					int ai3[] = vertexIndicesByBone[j3];
					for (int k4 = 0; k4 < ai3.length; k4++) {
						int l5 = ai3[k4];
						verticesX[l5] -= transformationPivotX;
						verticesY[l5] -= transformationPivotY;
						verticesZ[l5] -= transformationPivotZ;
						verticesX[l5] = (verticesX[l5] * j) / 128;
						verticesY[l5] = (verticesY[l5] * k) / 128;
						verticesZ[l5] = (verticesZ[l5] * l) / 128;
						verticesX[l5] += transformationPivotX;
						verticesY[l5] += transformationPivotY;
						verticesZ[l5] += transformationPivotZ;
					}

				}
			}

			return;
		}
		if (i == 5 && faceIndicesByBone != null && faceTransparency != null) {
			for (int j2 = 0; j2 < i1; j2++) {
				int k3 = ai[j2];
				if (k3 < faceIndicesByBone.length) {
					int ai4[] = faceIndicesByBone[k3];
					for (int l4 = 0; l4 < ai4.length; l4++) {
						int i6 = ai4[l4];
						faceTransparency[i6] += j * 8;
						if (faceTransparency[i6] < 0)
							faceTransparency[i6] = 0;
						if (faceTransparency[i6] > 255)
							faceTransparency[i6] = 255;
					}

				}
			}

		}
	}

	/**
	 * Rotates the model 90 degrees around the Y-axis.
	 */
	public void rotate90Y() {
		for (int i = 0; i < verticesCount; i++) {
			int j = verticesX[i];
			verticesX[i] = verticesZ[i];
			verticesZ[i] = -j;
		}
	} //TODO MIGHT NEED A RotateY method

	/**
	 * Rotates the model around the X-axis.
	 *
	 * @param angle The angle to rotate, indexed into the sine/cosine tables.
	 */
	public void rotateX(int angle) {
		int sin = sineTable[angle];
		int cos = cosineTable[angle];

		for (int i = 0; i < verticesCount; i++) {
			int y = verticesY[i] * cos - verticesZ[i] * sin >> 16;
			verticesZ[i] = verticesY[i] * sin + verticesZ[i] * cos >> 16;
			verticesY[i] = y;
		}
	}

	public void translate(int x, int y, int z) {
		for (int i = 0; i < verticesCount; i++) {
			verticesX[i] += x;
			verticesY[i] += y;
			verticesZ[i] += z;
		}
	}

	public void replaceColor(int oldColor, int newColor) {
		for (int i = 0; i < faceCount; i++)
			if (colors[i] == oldColor)
				colors[i] = newColor;
	}

	/**
	 * Mirrors the model along the Z-axis (across the XY plane).
	 * This operation negates the Z-coordinates and reverses the face winding
	 * order to ensure normals remain pointing outward.
	 */
	public void mirrorZ() {
		// Negate the Z coordinate for all vertices
		for (int i = 0; i < verticesCount; i++)
			verticesZ[i] = -verticesZ[i];

		// Reverse the triangle winding order to prevent the model from appearing inside-out
		for (int i = 0; i < faceCount; i++) {
			int tempIndex = faceIndicesX[i];
			faceIndicesX[i] = faceIndicesZ[i];
			faceIndicesZ[i] = tempIndex;
		}
	}

	/**
	 * Scales the model along the X, Y, and Z axes.
	 *
	 * @param scaleX The scale factor for the X-axis (128 = 100%).
	 * @param scaleY The scale factor for the Y-axis (128 = 100%).
	 * @param scaleZ The scale factor for the Z-axis (128 = 100%).
	 */
	public void resizeModel(int scaleX, int scaleY, int scaleZ) {
		for (int i = 0; i < verticesCount; i++) {
			verticesX[i] = (verticesX[i] * scaleX) / 128;
			verticesY[i] = (verticesY[i] * scaleY) / 128;
			verticesZ[i] = (verticesZ[i] * scaleZ) / 128;
		}
	}

	/**
	 * Initializes the lighting and normals for the model.
	 *
	 * @param ambient          The base ambient light level.
	 * @param lightIntensity   The intensity of the light source.
	 * @param lightX           X-direction of the light.
	 * @param lightY           Y-direction of the light.
	 * @param lightZ           Z-direction of the light.
	 * @param immediateShading If true, Gouraud shading is applied immediately.
	 */
	public void initLighting(int ambient, int lightIntensity, int lightX, int lightY, int lightZ, boolean immediateShading) {
		int lightMagnitude = (int) Math.sqrt(lightX * lightX + lightY * lightY + lightZ * lightZ);
		int scaledIntensity = lightIntensity * lightMagnitude >> 8;

		// Initialize shading arrays if they don't exist
		if (faceColorsA == null) {
			faceColorsA = new int[faceCount];
			faceColorsB = new int[faceCount];
			faceColorsC = new int[faceCount];
		}

		// Initialize vertex normals if they don't exist
		if (super.normals == null) {
			super.normals = new VertexNormal[verticesCount];
			for (int i = 0; i < verticesCount; i++) {
				super.normals[i] = new VertexNormal();
			}
		}

		for (int i = 0; i < faceCount; i++) {

			int vA = faceIndicesX[i];
			int vB = faceIndicesY[i];
			int vC = faceIndicesZ[i];

			//Edges Vectors
			int edgeX1 = verticesX[vB] - verticesX[vA];
			int edgeY1 = verticesY[vB] - verticesY[vA];
			int edgeZ1 = verticesZ[vB] - verticesZ[vA];

			int edgeX2 = verticesX[vC] - verticesX[vA];
			int edgeY2 = verticesY[vC] - verticesY[vA];
			int edgeZ2 = verticesZ[vC] - verticesZ[vA];

			int normalX = edgeY1 * edgeZ2 - edgeY2 * edgeZ1;
			int normalY = edgeZ1 * edgeX2 - edgeZ2 * edgeX1;
			int normalZ = edgeX1 * edgeY2 - edgeX2 * edgeY1;

			//Scale down to prevent overflow (The Scaling Guard)
			while(normalX > MAX_NORMAL_COMPONENT || normalY > MAX_NORMAL_COMPONENT || normalZ > MAX_NORMAL_COMPONENT ||
				  normalX < -MAX_NORMAL_COMPONENT || normalY < -MAX_NORMAL_COMPONENT || normalZ < -MAX_NORMAL_COMPONENT) {
				normalX >>= 1;
				normalY >>= 1;
				normalZ >>= 1;
			}

			int magnitude = (int) Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ); //TODO add + 0.5

			if (magnitude <= 0) {
				magnitude = 1;
			}

			// Normalize the vector components to the engine's standard scale (256)
			normalX = (normalX * 256) / magnitude;
			normalY = (normalY * 256) / magnitude;
			normalZ = (normalZ * 256) / magnitude;

			boolean isSmoothShaded = (faceRenderTypes == null) || (faceRenderTypes[i] & RENDER_TYPE_FLAT_SHADING) == 0;

			if(isSmoothShaded) {
				accumulateVertexNormal(vA, normalX, normalY, normalZ);
				accumulateVertexNormal(vB, normalX, normalY, normalZ);
				accumulateVertexNormal(vC, normalX, normalY, normalZ);
			} else {
				int lightDotProduct = (lightX * normalX + lightY * normalY + lightZ * normalZ);
				int faceIntensity = ambient + lightDotProduct / (scaledIntensity + scaledIntensity / 2);
				faceColorsA[i] = applyLightToColor(colors[i], faceIntensity, faceRenderTypes[i]);
			}
		}

		if (immediateShading) {
			calculateShading(ambient, scaledIntensity, lightX, lightY, lightZ);
			calculateRadius();
		} else {
			vertexNormalsTable = new VertexNormal[verticesCount];
			for (int i = 0; i < verticesCount; i++) {
				VertexNormal source = super.normals[i];
				VertexNormal destination = vertexNormalsTable[i] = new VertexNormal();
				destination.x = source.x;
				destination.y = source.y;
				destination.z = source.z;
				destination.magnitude = source.magnitude;
			}

			lightingParameters = (ambient << 16) | (scaledIntensity & 0xffff);

			// Calculate the full Axis-Aligned Bounding Box (AABB)
			calculateAABB();
		}
	}

	/**
	 * Adds a face normal to a vertex's accumulated normal.
	 * This is used to calculate the average normal for Gouraud (smooth) shading.
	 */
	private void accumulateVertexNormal(int vertexIndex, int nx, int ny, int nz) {
		VertexNormal vNorm = super.normals[vertexIndex];
		vNorm.x += nx;
		vNorm.y += ny;
		vNorm.z += nz;
		vNorm.magnitude++; // Tracks how many faces contribute to this vertex
	}

	/**
	 * Re-calculates the shading of the model using stored ambient and intensity values
	 * combined with a new light source direction.
	 *
	 * @param lightX The X-coordinate of the light source.
	 * @param lightY The Y-coordinate of the light source.
	 * @param lightZ The Z-coordinate of the light source.
	 */
	public void reapplyLighting(int lightX, int lightY, int lightZ) {
		// High 16 bits: Ambient, Low 16 bits: Light Magnitude
		int ambient = lightingParameters >> 16;
		int magnitude = (lightingParameters << 16) >> 16;

		calculateShading(ambient, magnitude, lightX, lightY, lightZ);
	}

	/**
	 * Calculates the Gouraud shading for the model based on a light source vector.
	 *
	 * @param ambient   The base ambient light level.
	 * @param magnitude The intensity/magnitude of the light source.
	 * @param lightX    X-component of the light direction.
	 * @param lightY    Y-component of the light direction.
	 * @param lightZ    Z-component of the light direction.
	 */
	public void calculateShading(int ambient, int magnitude, int lightX, int lightY, int lightZ) {
		for (int face = 0; face < faceCount; face++) {

			// Get the vertex indices for this face
			int vA = faceIndicesX[face];
			int vB = faceIndicesY[face];
			int vC = faceIndicesZ[face];

			// Determine if this face has specific render properties (like flat shading or textures)
			int renderType = (faceRenderTypes == null) ? 0 : faceRenderTypes[face];

			faceColorsA[face] = calculateVertexLight(vA, ambient, magnitude, lightX, lightY, lightZ, colors[face], renderType);
			faceColorsB[face] = calculateVertexLight(vB, ambient, magnitude, lightX, lightY, lightZ, colors[face], renderType);
			faceColorsC[face] = calculateVertexLight(vC, ambient, magnitude, lightX, lightY, lightZ, colors[face], renderType);

		}

		// Cleanup resources no longer needed after shading is baked into vertex colors
		super.normals = null;
		vertexNormalsTable = null;
		vertexBoneIds = null;
		faceBoneIds = null;
		if (faceRenderTypes != null) {
			for (int i = 0; i < faceCount; i++)
				if ((faceRenderTypes[i] & 2) == 2)
					return;
		}
		colors = null;
	}

	/**
	 * Helper to calculate the light intensity for a specific vertex using the Dot Product.
	 */
	private int calculateVertexLight(int vertexIndex, int ambient, int magnitude, int lx, int ly, int lz, int color, int type) {
		VertexNormal normal = super.normals[vertexIndex];

		// Calculate the divisor for the lighting equation
		int divisor = magnitude * normal.magnitude;
		int intensity;

		if (divisor != 0) {
			// Calculate the dot product between the light vector and the vertex normal
			int dotProduct = (lx * normal.x + ly * normal.y + lz * normal.z);
			intensity = ambient + (dotProduct / divisor);
		} else {
			// Fallback: If light magnitude or normal magnitude is zero,
			// the vertex only receives ambient light.
			intensity = ambient;
		}

		// method597 likely applies the intensity to the HSL/RGB color space
		return applyLightToColor(color, intensity, type);
	}

	/**
	 * Applies calculated light intensity to a packed HSL color.
	 *
	 * @param packedHsl      The base color (High bits: Hue/Sat, Low 7 bits: Lightness).
	 * @param lightIntensity The intensity of light calculated from the 3D scene (0-127).
	 * @param renderConfig   Bitmask for face properties (Bit 2: Inversion/Grayscale).
	 * @return The light-adjusted packed HSL color.
	 */
	public static int applyLightToColor(int packedHsl, int lightIntensity, int renderConfig) {
		// Check for special "Inversion" or Grayscale flag
		if ((renderConfig & 0x2) == 2) {
			if (lightIntensity < 0) {
				lightIntensity = 0;
			} else if (lightIntensity > 127) {
				lightIntensity = 127;
			}
			return 127 - lightIntensity;
		}

		// Extract the original lightness (bottom 7 bits)
		int baseLightness = packedHsl & 0x7f;

		// Apply lighting: (Intensity * BaseLightness) / 128
		int finalLightness = (lightIntensity * baseLightness) >> 7;

		// Clamp values to keep them within the visible 2-126 range
		if (finalLightness < 2) {
			finalLightness = 2;
		} else if (finalLightness > 126) {
			finalLightness = 126;
		}

		// Recombine the original Hue/Sat with the new Lightness
		return (packedHsl & 0xff80) | finalLightness;
	}

	/**
	 * Projects the model's vertices from local space to 24-bit fixed-point screen space.
	 *
	 * @param pitch      Rotation around the X-axis.
	 * @param yaw        Rotation around the Y-axis.
	 * @param roll       Rotation around the Z-axis.
	 * @param viewPitch  The pitch/tilt of the camera view.
	 * @param offsetX    Translation along the X-axis.
	 * @param offsetY    Translation along the Y-axis.
	 * @param offsetZ    Translation along the Z-axis.
	 */
	public void viewportTransform(int pitch, int yaw, int roll, int viewPitch, int offsetX, int offsetY, int offsetZ) {
		int centerX = ThreeDimensionalCanvas.centerX;
		int centerY = ThreeDimensionalCanvas.centerY;

		// Pre-calculate Trigonometry for the rotation matrix
		int sinPitch = sineTable[pitch];
		int cosPitch = cosineTable[pitch];
		int sinYaw = sineTable[yaw];
		int cosYaw = cosineTable[yaw];
		int sinRoll = sineTable[roll];
		int cosRoll = cosineTable[roll];
		int sinView = sineTable[viewPitch];
		int cosView = cosineTable[viewPitch];

		// Calculate a Z-depth offset based on camera height and tilt
		int viewZOffset = offsetY * sinView + offsetZ * cosView >> 16;

		for (int v = 0; v < verticesCount; v++) {
			int x = verticesX[v];
			int y = verticesY[v];
			int z = verticesZ[v];

			// Apply Roll (Rotation around Z-axis)
			if (roll != 0) {
				int tempX = y * sinRoll + x * cosRoll >> 16;
				y = y * cosRoll - x * sinRoll >> 16;
				x = tempX;
			}

			// Apply Pitch (Rotation around X-axis)
			if (pitch != 0) {
				int tempY = y * cosPitch - z * sinPitch >> 16;
				z = y * sinPitch + z * cosPitch >> 16;
				y = tempY;
			}

			// Apply Yaw (Rotation around Y-axis)
			if (yaw != 0) {
				int tempX = z * sinYaw + x * cosYaw >> 16;
				z = z * cosYaw - x * sinYaw >> 16;
				x = tempX;
			}

			// Translation (Move to world position)
			x += offsetX;
			y += offsetY;
			z += offsetZ;

			// Final Camera Transformation (Apply view tilt)
			int transformedY = y * cosView - z * sinView >> 16;
			z = y * sinView + z * cosView >> 16;
			y = transformedY;

			// Perspective Projection
			// Formula: screenPos = center + (worldPos * focalLength) / depth
			// The value 512 (1 << 9) represents the focal length/field of view.
			projectedZ[v] = z - viewZOffset;
			projectedX[v] = centerX + (x << 9) / z;
			projectedY[v] = centerY + (y << 9) / z;
			if (textureVertexCount > 0) {
				cameraX[v] = x;
				cameraY[v] = y;
				cameraZ[v] = z;
			}
		}

		try {
			processFaces(false, false, 0);
		} catch (Exception _ex) {
			// Catching exceptions here prevents a single malformed model
			// from crashing the entire frame's render loop.
		}
	}

	/**
	 * Renders the model as an entity within the game world, including frustum culling
	 * and mouse-hover detection.
	 *
	 * @param yaw           The model's local rotation around the Y-axis.
	 * @param sinViewPitch  Sine of the camera's pitch.
	 * @param cosViewPitch  Cosine of the camera's pitch.
	 * @param sinViewYaw    Sine of the camera's yaw.
	 * @param cosViewYaw    Cosine of the camera's yaw.
	 * @param relativeX     World X-translation relative to the camera.
	 * @param relativeY     World Y-translation relative to the camera.
	 * @param relativeZ     World Z-translation relative to the camera.
	 * @param modelId       Unique ID used for mouse-picking/interaction.
	 */
	@Override
	public void render(int yaw, int sinViewPitch, int cosViewPitch, int sinViewYaw, int cosViewYaw, int relativeX, int relativeY, int relativeZ, int modelId) {

		//Initial View Transformation (Camera Space Calculation)
		int transformedZ = relativeZ * cosViewYaw - relativeX * sinViewYaw >> 16;
		int depthZ = relativeY * sinViewPitch + transformedZ * cosViewPitch >> 16;
		int radiusZ = modelRadius * cosViewPitch >> 16;
		int maxZ = depthZ + radiusZ;

		// Frustum Culling (Z-Axis)
		// If the entire model is behind the near plane (50) or too far away, stop.
		if (maxZ <= 50 || depthZ >= 3500)
			return;

		int transformedX = relativeZ * sinViewYaw + relativeX * cosViewYaw >> 16;
		int leftLimit = transformedX - modelRadius << 9;

		// Frustum Culling (Horizontal/X-Axis)
		if (leftLimit / maxZ >= Drawable.anInt1432)
			return;

		int rightLimit = transformedX + modelRadius << 9;

		if (rightLimit / maxZ <= -Drawable.anInt1432)
			return;

		int transformedY = relativeY * cosViewPitch - transformedZ * sinViewPitch >> 16;
		int radiusY = modelRadius * sinViewPitch >> 16;
		int topLimit = transformedY + radiusY << 9;

		// Frustum Culling (Vertical/Y-Axis)
		if (topLimit / maxZ <= -Drawable.anInt1433)
			return;

		int bottomLimit = radiusY + (super.height * cosViewPitch >> 16);
		int bottomBound = transformedY - bottomLimit << 9;

		if (bottomBound / maxZ >= Drawable.anInt1433)
			return;

		// Broad-Phase Mouse Picking
		// Check if the mouse cursor is roughly within the screen-space bounding box
		boolean isMouseOver = false;
		boolean needsClipping = depthZ - (radiusZ + (super.height * sinViewPitch >> 16)) <= 50;

		if (modelId > 0 && isPickingEnabled) {
			int minZ = depthZ - radiusZ;
			if (minZ <= 50)
				minZ = 50;

			// Perspective-correct bounds for mouse check
			if (transformedX > 0) {
				leftLimit /= maxZ;
				rightLimit /= minZ;
			} else {
				rightLimit /= maxZ;
				leftLimit /= minZ;
			}

			if (transformedY > 0) {
				bottomBound /= maxZ;
				topLimit /= minZ;
			} else {
				topLimit /= maxZ;
				bottomBound /= minZ;
			}

			int mouseRelX = mouseX - ThreeDimensionalCanvas.centerX;
			int mouseRelY = mouseY - ThreeDimensionalCanvas.centerY;
			if (mouseRelX > leftLimit && mouseRelX < rightLimit && mouseRelY > bottomBound && mouseRelY < topLimit)
				if (isPriorityPicking)
					hoveredModels[hoveredCount++] = modelId;
				else
					isMouseOver = true;
		}

		// Vertex Transformation Loop
		int screenCenterX = ThreeDimensionalCanvas.centerX;
		int screenCenterY = ThreeDimensionalCanvas.centerY;
		int sinYaw = 0;
		int cosYaw = 0;

		if (yaw != 0) {
			sinYaw = sineTable[yaw];
			cosYaw = cosineTable[yaw];
		}

		for (int v = 0; v < verticesCount; v++) {
			int vX = verticesX[v];
			int vY = verticesY[v];
			int vZ = verticesZ[v];

			if (yaw != 0) {
				int rotateX = vZ * sinYaw + vX * cosYaw >> 16;
				vZ = vZ * cosYaw - vX * sinYaw >> 16;
				vX = rotateX;
			}

			// Apply World Position + Camera Rotation
			vX += relativeX;
			vY += relativeY;
			vZ += relativeZ;


			int xView = vZ * sinViewYaw + vX * cosViewYaw >> 16;
			vZ = vZ * cosViewYaw - vX * sinViewYaw >> 16;
			vX = xView;

			int yView = vY * cosViewPitch - vZ * sinViewPitch >> 16;
			vZ = vY * sinViewPitch + vZ * cosViewPitch >> 16;
			vY = yView;

			// Apply Projection
			projectedZ[v] = vZ - depthZ;

			if (vZ >= 50) {
				projectedX[v] = screenCenterX + (vX << 9) / vZ;
				projectedY[v] = screenCenterY + (vY << 9) / vZ;
			} else {
				projectedX[v] = -5000;
				needsClipping = true;
			}
			if (needsClipping || textureVertexCount > 0) {
				cameraX[v] = vX;
				cameraY[v] = vY;
				cameraZ[v] = vZ;
			}
		}

		// Rasterization and Face Processing
		try {
			processFaces(needsClipping, isMouseOver, modelId);
		} catch (Exception _ex) {
			// Catching exceptions here prevents a single malformed model
			// from crashing the entire frame's render loop.
			// In a production environment, consider logging this exception for debugging.
		}
	}

	/**
	 * Processes, sorts, and dispatches faces for rendering.
	 *
	 * <p>This method implements the core visibility pipeline:
	 * 1. Backface Culling: Discards triangles facing away from the camera.
	 * 2. Mouse Picking: Checks for per-face interaction.
	 * 3. Painter's Algorithm: Sorts faces into depth bins (0 to totalDepthSortingRange).
	 * 4. Priority Layering: Handles complex Z-ordering overrides (12 priority levels).</p>
	 *
	 * @param needsClipping   True if the model is near the camera's near-plane.
	 * @param isMouseOver     True if the mouse is within the model's broad bounding box.
	 * @param modelId         The unique ID for interaction events.
	 */
	public void processFaces(boolean needsClipping, boolean isMouseOver, int modelId) {
		// Clear the depth bin counters
		for (int i = 0; i < totalDepthSortingRange; i++) {
			faceDepthCounts[i] = 0;
		}

		// Culling, Picking, and Depth Sorting Loop
		for (int face = 0; face < faceCount; face++) {

			// Skip hidden faces
			if (faceRenderTypes != null && faceRenderTypes[face] == -1) {
				continue;
			}

			int vA = faceIndicesX[face];
			int vB = faceIndicesY[face];
			int vC = faceIndicesZ[face];

			int xA = projectedX[vA];
			int xB = projectedX[vB];
			int xC = projectedX[vC];

			// Handle Near-Plane Clipping logic
			// -5000 is the sentinel value for vertices projected behind the camera
			if (needsClipping && (xA == -5000 || xB == -5000 || xC == -5000)) {
				faceNeedsClipping[face] = true;

				// Calculate average Z depth and add to bin
				int avgZ = (projectedZ[vA] + projectedZ[vB] + projectedZ[vC]) / 3 + modelBoundingSphere;
				faceDepthBins[avgZ][faceDepthCounts[avgZ]++] = face;
			} else {
				// Narrow-Phase Mouse Picking: Check if mouse is inside this specific triangle
				if (isMouseOver && isPointInFaceBounds(mouseX, mouseY, projectedY[vA], projectedY[vB], projectedY[vC], xA, xB, xC)) {
					hoveredModels[hoveredCount++] = modelId;
					isMouseOver = false;
				}

				// 2D Backface Culling (Cross Product check)
				// If the cross product of two edges is positive, the face is pointing towards the camera.
				if ((xA - xB) * (projectedY[vC] - projectedY[vB]) - (projectedY[vA] - projectedY[vB]) * (xC - xB) > 0) {
					faceNeedsClipping[face] = false;

					// Viewport Frustum Check (Horizontal Clipping)
					faceIsOffScreen[face] = xA < 0 || xB < 0 || xC < 0 || xA > Drawable.viewportRightBoundary || xB > Drawable.viewportRightBoundary || xC > Drawable.viewportRightBoundary;

					// Assign to depth bin based on average projected Z
					int avgZ = (projectedZ[vA] + projectedZ[vB] + projectedZ[vC]) / 3 + modelBoundingSphere;
					faceDepthBins[avgZ][faceDepthCounts[avgZ]++] = face;
				}
			}
		}

		if (facePriorities == null) {
			for (int i1 = totalDepthSortingRange - 1; i1 >= 0; i1--) {
				int l1 = faceDepthCounts[i1];
				if (l1 > 0) {
					int ai[] = faceDepthBins[i1];
					for (int j3 = 0; j3 < l1; j3++)
						drawFace(ai[j3]);

				}
			}

			return;
		}
		for (int j1 = 0; j1 < 12; j1++) {
			priorityCounts[j1] = 0;
			priorityAverages[j1] = 0;
		}

		for (int i2 = totalDepthSortingRange - 1; i2 >= 0; i2--) {
			int k2 = faceDepthCounts[i2];
			if (k2 > 0) {
				int ai1[] = faceDepthBins[i2];
				for (int i4 = 0; i4 < k2; i4++) {
					int l4 = ai1[i4];
					int l5 = facePriorities[l4];
					int j6 = priorityCounts[l5]++;
					priorityBins[l5][j6] = l4;
					if (l5 < 10)
						priorityAverages[l5] += i2;
					else if (l5 == 10)
						priorityDepthX[j6] = i2;
					else
						priorityDepthY[j6] = i2;
				}

			}
		}

		int l2 = 0;
		if (priorityCounts[1] > 0 || priorityCounts[2] > 0)
			l2 = (priorityAverages[1] + priorityAverages[2]) / (priorityCounts[1] + priorityCounts[2]);
		int k3 = 0;
		if (priorityCounts[3] > 0 || priorityCounts[4] > 0)
			k3 = (priorityAverages[3] + priorityAverages[4]) / (priorityCounts[3] + priorityCounts[4]);
		int j4 = 0;
		if (priorityCounts[6] > 0 || priorityCounts[8] > 0)
			j4 = (priorityAverages[6] + priorityAverages[8]) / (priorityCounts[6] + priorityCounts[8]);
		int i6 = 0;
		int k6 = priorityCounts[10];
		int ai2[] = priorityBins[10];
		int ai3[] = priorityDepthX;
		if (i6 == k6) {
			i6 = 0;
			k6 = priorityCounts[11];
			ai2 = priorityBins[11];
			ai3 = priorityDepthY;
		}
		int i5;
		if (i6 < k6)
			i5 = ai3[i6];
		else
			i5 = -1000;
		for (int l6 = 0; l6 < 10; l6++) {
			while (l6 == 0 && i5 > l2) {
				drawFace(ai2[i6++]);
				if (i6 == k6 && ai2 != priorityBins[11]) {
					i6 = 0;
					k6 = priorityCounts[11];
					ai2 = priorityBins[11];
					ai3 = priorityDepthY;
				}
				if (i6 < k6)
					i5 = ai3[i6];
				else
					i5 = -1000;
			}
			while (l6 == 3 && i5 > k3) {
				drawFace(ai2[i6++]);
				if (i6 == k6 && ai2 != priorityBins[11]) {
					i6 = 0;
					k6 = priorityCounts[11];
					ai2 = priorityBins[11];
					ai3 = priorityDepthY;
				}
				if (i6 < k6)
					i5 = ai3[i6];
				else
					i5 = -1000;
			}
			while (l6 == 5 && i5 > j4) {
				drawFace(ai2[i6++]);
				if (i6 == k6 && ai2 != priorityBins[11]) {
					i6 = 0;
					k6 = priorityCounts[11];
					ai2 = priorityBins[11];
					ai3 = priorityDepthY;
				}
				if (i6 < k6)
					i5 = ai3[i6];
				else
					i5 = -1000;
			}
			int i7 = priorityCounts[l6];
			int ai4[] = priorityBins[l6];
			for (int j7 = 0; j7 < i7; j7++)
				drawFace(ai4[j7]);

		}

		while (i5 != -1000) {
			drawFace(ai2[i6++]);
			if (i6 == k6 && ai2 != priorityBins[11]) {
				i6 = 0;
				ai2 = priorityBins[11];
				k6 = priorityCounts[11];
				ai3 = priorityDepthY;
			}
			if (i6 < k6)
				i5 = ai3[i6];
			else
				i5 = -1000;
		}
	}

	/**
	 * Dispatches a face to the rasterizer based on its render configuration.
	 *
	 * @param faceId The index of the face to be drawn.
	 */
	public void drawFace(int faceId) {
		// Handle Near-Plane Clipping
		if (faceNeedsClipping[faceId]) {
			clipAndDrawFace(faceId);
			return;
		}

		// Fetch Vertex Indices
		int vA = faceIndicesX[faceId];
		int vB = faceIndicesY[faceId];
		int vC = faceIndicesZ[faceId];

		// Sync Global Rasterizer State
		ThreeDimensionalCanvas.requiresBoundsCheck = faceIsOffScreen[faceId];

		if (faceTransparency == null)
			ThreeDimensionalCanvas.currentFaceAlpha = 0;
		else
			ThreeDimensionalCanvas.currentFaceAlpha = faceTransparency[faceId];

		// Determine Render Mode (Bits 0-1)
		int renderMode = (faceRenderTypes == null) ? 0 : faceRenderTypes[faceId] & 3;

		switch(renderMode) {
			case 0: // Gouraud (Smooth) Shading
				ThreeDimensionalCanvas.drawGouraudTriangle(projectedY[vA], projectedY[vB], projectedY[vC],
						projectedX[vA], projectedX[vB], projectedX[vC], faceColorsA[faceId], faceColorsB[faceId],
						faceColorsC[faceId]);
				break;
			case 1: // Flat Shading
				ThreeDimensionalCanvas.drawFlatTriangle(projectedY[vA], projectedY[vB], projectedY[vC],
						projectedX[vA], projectedX[vB], projectedX[vC], colorLookupTable[faceColorsA[faceId]]);
				break;
			case 2: case 3:
				// Delegate to a helper method for textured faces, as they share common parameters
				// The textureIndex variable is now scoped within drawTexturedFace
				drawTexturedFace(faceId, vA, vB, vC, renderMode);
				break;
		}
	}

	/**
	 * Internal helper to handle the complex parameter list for textured triangles.
	 * This method is called by drawFace when the renderMode indicates a textured face.
	 */
	private void drawTexturedFace(int faceId, int vA, int vB, int vC, int mode) {
		int textureIndex = faceRenderTypes[faceId] >> 2;
		int tA = textureVertexIndicesA[textureIndex];
		int tB = textureVertexIndicesB[textureIndex];
		int tC = textureVertexIndicesC[textureIndex];

		int colorA = faceColorsA[faceId];
		int colorB = (mode == 3) ? colorA : faceColorsB[faceId]; // Handles the difference between mode 2 and 3
		int colorC = (mode == 3) ? colorA : faceColorsC[faceId]; // Handles the difference between mode 2 and 3

		ThreeDimensionalCanvas.drawTexturedTriangle(projectedY[vA], projectedY[vB], projectedY[vC],
				projectedX[vA], projectedX[vB], projectedX[vC], colorA, colorB,
				colorC, cameraX[tA], cameraX[tB], cameraX[tC], cameraY[tA],
				cameraY[tB], cameraY[tC], cameraZ[tA], cameraZ[tB], cameraZ[tC],
				colors[faceId]);
	}

	/**
	 * Clips a face against the near-plane (Z=50) and dispatches it to the rasterizer.
	 *
	 * <p>When a face crosses the near-plane, this method calculates the intersection
	 * points to create a new visible polygon. If the clipped result is a triangle,
	 * it is drawn directly. If it results in a quadrilateral, it is tessellated into
	 * two triangles.</p>
	 *
	 * @param faceId The index of the face to clip and draw.
	 */
	public void clipAndDrawFace(int faceId) {
		int centerX = ThreeDimensionalCanvas.centerX;
		int centerY = ThreeDimensionalCanvas.centerY;
		int clippedCount = 0;

		int vA = faceIndicesX[faceId];
		int vB = faceIndicesY[faceId];
		int vC = faceIndicesZ[faceId];

		int zA = cameraZ[vA];
		int zB = cameraZ[vB];
		int zC = cameraZ[vC];

		final int NEAR_PLANE = 50;

		// --- Vertex A Clipping Logic ---
		if (zA >= NEAR_PLANE) {
			clippedProjectedX[clippedCount] = projectedX[vA];
			clippedProjectedY[clippedCount] = projectedY[vA];
			clippedVertexColors[clippedCount++] = faceColorsA[faceId];
		} else {
			int xA = cameraX[vA];
			int yA = cameraY[vA];
			int colorA = faceColorsA[faceId];

			if (zC >= NEAR_PLANE) { //Edge A-C crosses near plane
				int lerpRatio = (NEAR_PLANE - zA) * reciprocalTable[zC - zA];
				clippedProjectedX[clippedCount] = centerX + (xA + ((cameraX[vC] - xA) * lerpRatio >> 16) << 9) / NEAR_PLANE;
				clippedProjectedY[clippedCount] = centerY + (yA + ((cameraY[vC] - yA) * lerpRatio >> 16) << 9) / NEAR_PLANE;
				clippedVertexColors[clippedCount++] = colorA + ((faceColorsC[faceId] - colorA) * lerpRatio >> 16);
			}
			if (zB >= NEAR_PLANE) {
				int lerpRatio = (NEAR_PLANE - zA) * reciprocalTable[zB - zA];
				clippedProjectedX[clippedCount] = centerX + (xA + ((cameraX[vB] - xA) * lerpRatio >> 16) << 9) / NEAR_PLANE;
				clippedProjectedY[clippedCount] = centerY + (yA + ((cameraY[vB] - yA) * lerpRatio >> 16) << 9) / NEAR_PLANE;
				clippedVertexColors[clippedCount++] = colorA + ((faceColorsB[faceId] - colorA) * lerpRatio >> 16);
			}
		}

		// --- Vertex B Clipping Logic ---
		if (zB >= NEAR_PLANE) {
			clippedProjectedX[clippedCount] = projectedX[vB];
			clippedProjectedY[clippedCount] = projectedY[vB];
			clippedVertexColors[clippedCount++] = faceColorsB[faceId];
		} else {
			int xB = cameraX[vB];
			int yB = cameraY[vB];
			int colorB = faceColorsB[faceId];
			if (zA >= NEAR_PLANE) {
				int lerpRatio = (NEAR_PLANE - zB) * reciprocalTable[zA - zB];
				clippedProjectedX[clippedCount] = centerX + (xB + ((cameraX[vA] - xB) * lerpRatio >> 16) << 9) / NEAR_PLANE;
				clippedProjectedY[clippedCount] = centerY + (yB + ((cameraY[vA] - yB) * lerpRatio >> 16) << 9) / NEAR_PLANE;
				clippedVertexColors[clippedCount++] = colorB + ((faceColorsA[faceId] - colorB) * lerpRatio >> 16);
			}
			if (zC >= NEAR_PLANE) {
				int lerpRatio = (NEAR_PLANE - zB) * reciprocalTable[zC - zB];
				clippedProjectedX[clippedCount] = centerX + (xB + ((cameraX[vC] - xB) * lerpRatio >> 16) << 9) / NEAR_PLANE;
				clippedProjectedY[clippedCount] = centerY + (yB + ((cameraY[vC] - yB) * lerpRatio >> 16) << 9) / NEAR_PLANE;
				clippedVertexColors[clippedCount++] = colorB + ((faceColorsC[faceId] - colorB) * lerpRatio >> 16);
			}
		}

		// --- Vertex C Clipping Logic ---
		if (zC >= NEAR_PLANE) {
			clippedProjectedX[clippedCount] = projectedX[vC];
			clippedProjectedY[clippedCount] = projectedY[vC];
			clippedVertexColors[clippedCount++] = faceColorsC[faceId];
		} else {
			int xC = cameraX[vC];
			int yC = cameraY[vC];
			int colorC = faceColorsC[faceId];
			if (zB >= NEAR_PLANE) {
				int lerpRatio = (NEAR_PLANE - zC) * reciprocalTable[zB - zC];
				clippedProjectedX[clippedCount] = centerX + (xC + ((cameraX[vB] - xC) * lerpRatio >> 16) << 9) / NEAR_PLANE;
				clippedProjectedY[clippedCount] = centerY + (yC + ((cameraY[vB] - yC) * lerpRatio >> 16) << 9) / NEAR_PLANE;
				clippedVertexColors[clippedCount++] = colorC + ((faceColorsB[faceId] - colorC) * lerpRatio >> 16);
			}
			if (zA >= NEAR_PLANE) {
				int lerpRatio = (NEAR_PLANE - zC) * reciprocalTable[zA - zC];
				clippedProjectedX[clippedCount] = centerX + (xC + ((cameraX[vA] - xC) * lerpRatio >> 16) << 9) / 50;
				clippedProjectedY[clippedCount] = centerY + (yC + ((cameraY[vA] - yC) * lerpRatio >> 16) << 9) / 50;
				clippedVertexColors[clippedCount++] = colorC + ((faceColorsA[faceId] - colorC) * lerpRatio >> 16);
			}
		}

		// Fail-safe: If after clipping we have less than 3 vertices, we cannot form a valid polygon to render.
		if (clippedCount < 3) return;

		int x1 = clippedProjectedX[0], x2 = clippedProjectedX[1], x3 = clippedProjectedX[2];
		int y1 = clippedProjectedY[0], y2 = clippedProjectedY[1], y3 = clippedProjectedY[2];

		// Backface Culling Check (Cross Product)
		if ((x1 - x2) * (y3 - y2) - (y1 - y2) * (x3 - x2) > 0) {

			// Push rendering state to Canvas
			ThreeDimensionalCanvas.requiresBoundsCheck = isOffScreen(clippedCount);
			int renderMode = (faceRenderTypes == null) ? 0 : (faceRenderTypes[faceId] & 3);

			// Draw the first triangle
			renderClippedTriangle(faceId, renderMode, 0, 1, 2);

			// If it's a quad, draw the second triangle (0, 2, 3)
			if (clippedCount == 4) {
				renderClippedTriangle(faceId, renderMode, 0, 2, 3);
			}
		}
	}

	/**
	 * Checks if any vertices in the clipped polygon are off-screen to enable rasterizer clipping.
	 */
	private boolean isOffScreen(int count) {
		for (int i = 0; i < count; i++) {
			if (clippedProjectedX[i] < 0 || clippedProjectedX[i] > Drawable.viewportRightBoundary) return true;
		}
		return false;
	}

	/**
	 * Helper to dispatch drawing for clipped polygon segments.
	 */
	private void renderClippedTriangle(int faceId, int mode, int idx1, int idx2, int idx3) {
		int xA = clippedProjectedX[idx1], xB = clippedProjectedX[idx2], xC = clippedProjectedX[idx3];
		int yA = clippedProjectedY[idx1], yB = clippedProjectedY[idx2], yC = clippedProjectedY[idx3];
		int cA = clippedVertexColors[idx1], cB = clippedVertexColors[idx2], cC = clippedVertexColors[idx3];

		switch(mode) {
			case 0:  // Gouraud triangle
				ThreeDimensionalCanvas.drawGouraudTriangle(yA, yB, yC, xA, xB, xC, cA, cB, cC);
				break;
			case 1: // Flat triangle
				ThreeDimensionalCanvas.drawFlatTriangle(yA, yB, yC, xA, xB, xC, colorLookupTable[faceColorsA[faceId]]);
				break;
			case 2: // Textured
			case 3: // Textured Flat
				int textureIndex = faceRenderTypes[faceId] >> 2;
				int texA = textureVertexIndicesA[textureIndex];
				int texB = textureVertexIndicesB[textureIndex];
				int texC = textureVertexIndicesC[textureIndex];

				// For Flat Textured, we override corners with the primary color
				int colorA = (mode == 3) ? faceColorsA[faceId] : cA;
				int colorB = (mode == 3) ? faceColorsA[faceId] : cB;
				int colorC = (mode == 3) ? faceColorsA[faceId] : cC;

				ThreeDimensionalCanvas.drawTexturedTriangle(yA, yB, yC, xA, xB, xC, colorA, colorB,
						colorC, cameraX[texA], cameraX[texB], cameraX[texC],
						cameraY[texA], cameraY[texB], cameraY[texC], cameraZ[texA],
						cameraZ[texB], cameraZ[texC], colors[faceId]);
				break;

		}
	}

	/**
	 * Performs a fast Axis-Aligned Bounding Box (AABB) intersection check
	 * to determine if a point (mouse) is within the screen-space extents of a face.
	 *
	 * @param mouseX The current X-position of the mouse.
	 * @param mouseY The current Y-position of the mouse.
	 * @param v1Y    Projected Y-coordinate of the first vertex.
	 * @param v2Y    Projected Y-coordinate of the second vertex.
	 * @param v3Y    Projected Y-coordinate of the third vertex.
	 * @param v1X    Projected X-coordinate of the first vertex.
	 * @param v2X    Projected X-coordinate of the second vertex.
	 * @param v3X    Projected X-coordinate of the third vertex.
	 * @return True if the point is within the face's bounding box; otherwise false.
	 */
	public boolean isPointInFaceBounds(int mouseX, int mouseY, int v1Y, int v2Y, int v3Y, int v1X, int v2X, int v3X) {
		if (mouseY < v1Y && mouseY < v2Y && mouseY < v3Y)
			return false;
		if (mouseY > v1Y && mouseY > v2Y && mouseY > v3Y)
			return false;
		if (mouseX < v1X && mouseX < v2X && mouseX < v3X)
			return false;

		return mouseX <= v1X || mouseX <= v2X || mouseX <= v3X;
	}
}
