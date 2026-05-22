package com.jagex.runescape;

/**
 * A descriptor containing the metadata and memory offsets for a raw 3D model.
 *
 * <p>The com.jagex.runescape.ModelHeader is used by the {@link Model} constructor to navigate the
 * raw byte array provided by the cache. It defines how many vertices and faces
 * exist, and where the specific data blocks (coordinates, colors, priorities,
 * and bone IDs) begin within the buffer.</p>
 */
public class ModelHeader {

	public ModelHeader() {
	}

	/**
	 * The raw binary data of the model retrieved from the cache.
	 */
	public byte[] rawModelData;

	/**
	 * The total number of vertices (points in 3D space) in the model.
	 */
	public int vertexCount;

	/**
	 * The total number of faces (triangles) in the model.
	 */
	public int faceCount;

	/**
	 * The number of texture mapping coordinates in the model.
	 */
	public int textureVertexCount;

	/**
	 * Offset to the bitmask flags for each vertex (used for coordinate compression).
	 */
	public int vertexFlagsOffset;

	/**
	 * Offset to the X-coordinate data block.
	 */
	public int vertexXOffset;

	/**
	 * Offset to the Y-coordinate data block.
	 */
	public int vertexYOffset;

	/**
	* Offset to the Z-coordinate data block.
	*/
	public int vertexZOffset;

	/**
	 * Offset to the bone/transformation IDs assigned to each vertex.
	 */
	public int vertexBoneOffset;

	/**
	 * Offset to the triangle vertex indices (the "connect the dots" instructions).
	 */
	public int faceIndicesOffset;

	/**
	 * Offset to the face type data (defines drawing order/topology).
	 */
	public int faceTypeOffset;

	/**
	 * Offset to the 16-bit HSL color data for each face.
	 */
	public int faceColorOffset;

	/**
	 * Offset to the rendering configuration for each face (Flat vs. Gouraud shading).
	 */
	public int faceRenderTypeOffset;

	/**
	 * Offset to the Z-depth sorting priority. If negative,
	 * a global priority is used instead.
	 */
	public int facePriorityOffset;

	/**
	 * Offset to the alpha transparency values for each face.
	 */
	public int faceTransparencyOffset;

	/**
	 * Offset to the bone/transformation IDs assigned to each face.
	 */
	public int faceBoneOffset;

	/**
	 * Offset to the UV mapping data for textured models.
	 */
	public int textureMappingOffset;
}
