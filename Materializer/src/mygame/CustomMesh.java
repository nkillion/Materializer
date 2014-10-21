package mygame;

import com.jme3.math.Vector3f;
import com.jme3.math.Vector2f;
import com.jme3.scene.Mesh;
import com.jme3.util.BufferUtils;
import com.jme3.scene.VertexBuffer.*;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 *** Generates a mesh, given the vertices of one side and a depth
 *		Vertices must be provided in either Clockwise or Counter-Clockwise order!!!
 * 
 * @param verts - Vector3f[], the vertices of one side of the mesh
 * @param depth - float, the depth of the mesh
 * @param vertColor - boolean, true for random vertex coloration
 * 
 * @author Nick Killion
 */
public class CustomMesh extends Mesh {
    
    private Vector3f[] frontVerts;
    private Vector3f[] backVerts;
    private int[] triangleIndicesArray;
    private ArrayList triangleIndices;
    private ArrayList dog;
    private int length; //num of verts per side
    private float depth;
    private boolean vertColor;
    private float[] colorArray;
    private float[] normals;
    public Vector3f topFrontPoint, bottomFrontPoint;
    public Vector3f center;

    public CustomMesh(Vector3f[] verts, float depth, boolean vertColor) {
        	
        frontVerts = verts.clone();
        length = verts.length;
	backVerts = new Vector3f[length];
        triangleIndices = new ArrayList();
	dog = new ArrayList();//keeps track of removed verts
        normals = new float[length * 18];   //x, y, and z for all vertices (front and back), each vertex is tripled. length * 3 * 2 * 3
	this.depth = depth;
	this.vertColor = vertColor;
        
	makeCCW();
	getCenterAtOrigin();
	getTopBottomVerts();
        generateBackSideVerts();
        generateFrontTriangles();
	generateBackTriangles();
	generateSideTriangles();
//        generateNormals();
	
	//4 floats to represent color for one vertex, and there are vertices for front and back, so length * 8
	if(vertColor) {
	    colorArray = new float[length * 8];
	    colorVerts();
	}
        
        finalizeMesh();
        
    }
    
    //constructor without color option, defaults to uncolored
    public CustomMesh(Vector3f[] verts, float depth) {
	this(verts, depth, false);
    }
    
    //ensure vertices are in counterclockwise order. if not, reverse their order
    private void makeCCW() {
	
	//get sum of edges
	float sum = 0;
	Vector2f v1, v2;
	int next;
	
	for (int i = 0; i < length; i++) {
	    next = iterate(i, 1);
	    
	    v1 = new Vector2f(frontVerts[i].x, frontVerts[i].y);
	    v2 = new Vector2f(frontVerts[next].x, frontVerts[next].y);
	    
	    sum += (v2.x - v1.x) * (v2.y + v1.y);
	}
	
	//if already CCW, return
	if (sum < 0)
	    return;
	
	//else, reverse order
	Vector3f temp;
	for(int i = 0; i < length / 2; i++) {
	    temp = frontVerts[i];
	    frontVerts[i] = frontVerts[length - i - 1];
	    frontVerts[length - i - 1] = temp;
	}
	    
    }
    
    //needed to create a ray to find orientation of mesh. topfront vertex and bottom front vertex
    private void getTopBottomVerts() {

	topFrontPoint = frontVerts[0];
	bottomFrontPoint = frontVerts[0];

	for (int i = 1; i < length; i++) {
	    if (frontVerts[i].y < bottomFrontPoint.y)
		bottomFrontPoint = frontVerts[i];
	    if (frontVerts[i].y > topFrontPoint.y)
		topFrontPoint = frontVerts[i];
	}

    }
    
    private void getCenterAtOrigin() {

	float x = 0;
	float y = 0;
	
	for (int i = 0; i < length; i++) {
	    x += frontVerts[i].x;
	    y += frontVerts[i].y;
	}
	 
	x /= length;
	y /= length;
	float z = depth / 2f;
	
	Vector3f center = new Vector3f(x, y, z);
	
	for (int i = 0; i < length; i++) {
	    frontVerts[i].y = frontVerts[i].y - center.y;
	    frontVerts[i].x = frontVerts[i].x - center.x;
	    frontVerts[i].z = frontVerts[i].z - center.z;
	}
	
    }
    
    
    
    //generates vertices for the other side of the mesh (frontSideVerts + a z-depth)
    private void generateBackSideVerts() {
        
        //generate back side vertices
        for (int j = 0; j < length; j++)
            backVerts[j] = new Vector3f(frontVerts[j].x, frontVerts[j].y, frontVerts[j].z + depth);
    }
    
    //dog-ear triangulation method
    private void generateFrontTriangles() {
        
        //the two lines incident to the vertex
        Vector2f sideOne, sideTwo;
        float sideOneX, sideOneY, sideTwoX, sideTwoY;
        
        int i = 0;
	
        while (dog.size() < (length - 3))  //end when only three vertices left (this is last triangle)
        {
	    //get indices
	    int next = iterate(i, 1);
	    int nextnext = iterate(i, 2);
	    
	    //get vectors
	    sideOneX = frontVerts[next].x - frontVerts[i].x;
	    sideOneY = frontVerts[next].y - frontVerts[i].y;
	    sideTwoX = frontVerts[nextnext].x - frontVerts[next].x;
	    sideTwoY = frontVerts[nextnext].y - frontVerts[next].y;            
	    sideOne = new Vector2f(sideOneX, sideOneY);
	    sideTwo = new Vector2f(sideTwoX, sideTwoY);
	    
	    //calculate cross product
	    sideOne = sideOne.normalize();
	    sideTwo = sideTwo.normalize();
	    float res = (sideOne.x * sideTwo.y) - (sideTwo.x * sideOne.y);
	    
	      //if left hand turn, and if this triangle does not contain any vertices
	    if (res > 0 && !triangleContainsVertex(i, next, nextnext)) { 
		addTriangle(nextnext, next, i);
		dog.add(next);
	    }
	    i = iterate(i, 1);
        }
	//final triangle
	addTriangle(iterate(i, 2), iterate(i,1), i);
        
    }
    
    private void generateBackTriangles() {
	
	int numTriangleVerts = triangleIndices.size();
	int v1, v2, v3;
	
	for (int i = 0; i < numTriangleVerts; i += 3) {
	    
	    v1 = length + (Integer) triangleIndices.get(i);
	    v2 = length + (Integer) triangleIndices.get(i+1);
	    v3 = length + (Integer) triangleIndices.get(i+2);
	    
	    addTriangle(v3, v2, v1);
	}
    }
    
    //randomly set a color for each vertex
    private void colorVerts() {
	Random rand = new Random();
	
	for (int i = 0; i < colorArray.length; i++)
	    colorArray[i] = rand.nextFloat();
    }
    
  
   private void generateSideTriangles() {
	for (int i = 0; i < length-1; i++) {
	    addTriangle(i, i+1, i+1+length);
	    addTriangle(i+1+length, i+length, i);
	}
	addTriangle(length-1, 0, length);
	addTriangle(length, length*2-1, length-1);
    }
    
   private void generateNormals() {
       int i;
       
       //one set of normals is z unit vector
       for (i = 0; i < length * 3; i += 3) {
           normals[i] = 0;
           normals[i + 1] = 0;
           normals[i + 2] = 1;
       }
       for (i = length * 3; i < length * 6; i += 3) {
           normals[i] = 0;
           normals[i + 1] = 0;
           normals[i + 2] = -1;
       }
      
       Vector3f v1, v2, cross;
       float v1x, v1y, v2x, v2y;
       
       for (i = length; i < length * 3; i += 3) {
           
           //vector from frontVerts[i] to frontVerts[i + 1]
           v1x = frontVerts[(i + 1) % length].x - frontVerts[i % length].x;
           v1y = frontVerts[(i + 1) % length].y - frontVerts[i % length].y;
           
           //vector from frontVerts[i] to backVerts[i + 1]
           v2x = backVerts[(i + 1) % length].x - frontVerts[i % length].x;
           v2y = backVerts[(i + 1) % length].y - frontVerts[i % length].y;
           
           v1 = new Vector3f(v1x, v1y, 0);
           v2 = new Vector3f(v2x, v2y, depth);

           //calculate cross product
           v1 = v1.normalize();
           v2 = v2.normalize();
           cross = v1.cross(v2);
           cross = cross.normalize();
           
           normals[i + length * 6] = cross.x;
           normals[i + 1 + length * 6] = cross.y;
           normals[i + 2 + length * 6] = cross.z;
           
           normals[i + length * 9] = cross.x;
           normals[i + 1 + length * 9] = cross.y;
           normals[i + 2 + length * 9] = cross.z;
           
           normals[i + length * 12] = cross.x;
           normals[i + 1 + length * 12] = cross.y;
           normals[i + 2 + length * 12] = cross.z;
           
           normals[i + length * 15] = cross.x;
           normals[i + 1 + length * 15] = cross.y;
           normals[i + 2 + length * 15] = cross.z;
       }
   }
   
   //returns vector from frontVerts[p1] to frontVerts[p2]
        //(frontVerts[p2] - frontVerts[p1])
   private Vector3f getVector(int p1, int p2) {
       return new Vector3f(frontVerts[p2].x - frontVerts[p1].x, frontVerts[p2].y - frontVerts[p1].y, 0);
   }
   
   //returns index of unremoved vertex, numIterations from i. modulo to allow cyclical traversal
    private int iterate(int i, int numIterations) {
	
	for (int j = 0; j < numIterations; j++)
	    while (dog.contains(++i % length)) {}

	if (i >= length)
	    i = i % length;
	
	return i;
    }
    
    
    //adds triangle to triangleIndices
    private void addTriangle(int v1, int v2, int v3) {
        triangleIndices.add(v1);
        triangleIndices.add(v2);
        triangleIndices.add(v3);
    }
    
    //returns true if the triangle represented by the three points (indices into frontVerts) contains any vertex
    private boolean triangleContainsVertex(int p1, int p2, int p3) {
	int i;
	
	//get triangle verts in Vector3f form
	Vector3f v1 = frontVerts[p1];
	Vector3f v2 = frontVerts[p2];
	Vector3f v3 = frontVerts[p3];
	
	//get bounding box, so not testing every vertex
	float minX = Math.min(v1.x, Math.min(v2.x, v3.x));
	float minY = Math.min(v1.y, Math.min(v2.y, v3.y));
	float maxX = Math.max(v1.x, Math.max(v2.x, v3.x));
	float maxY = Math.max(v1.y, Math.max(v2.y, v3.y));
	
	//get arraylist of possible vertices
	ArrayList possibleVerts = new ArrayList();
	
	float ix, iy;
	for(i = 0; i < length; i++) {
	    //if we are not looking at one of the vertices of the triangle
	    if (i != p1 && i != p2 && i != p3) {
		ix = frontVerts[i].x;
		iy = frontVerts[i].y;
		//and if we are looking at a vertex within the bounding box
		if (ix < maxX && ix > minX && iy < maxY && iy > minY)
		    possibleVerts.add(i);   //then add the point to possibleVerts
	    }
	}
	//if no possibleVerts, then return false
	if (possibleVerts.isEmpty())
	    return false;
	
	boolean ret = false;
	
	//get vectors representing line from v1 to v2, etc
	Vector2f v1v2 = new Vector2f(v2.x-v1.x, v2.y-v1.y);
	Vector2f v2v3 = new Vector2f(v3.x-v2.x, v3.y-v2.y);
	Vector2f v3v1 = new Vector2f(v1.x-v3.x, v1.y-v3.y);
	
	while (!possibleVerts.isEmpty() && !ret) {
	    //get point of interest as a Vector3f
	    Vector3f point = frontVerts[(Integer)possibleVerts.remove(0)];
	    
	    //get vectors representing line from v1 to point, etc
	    Vector2f v1point = new Vector2f(point.x-v1.x, point.y-v1.y);
	    Vector2f v2point = new Vector2f(point.x-v2.x, point.y-v2.y);
	    Vector2f v3point = new Vector2f(point.x-v3.x, point.y-v3.y);
	    
	    //check turn directions
	    float turn1 = v1v2.x * v2point.y - v1v2.y * v2point.x;
	    float turn2 = v2v3.x * v3point.y - v2v3.y * v3point.x;
	    float turn3 = v3v1.x * v1point.y - v3v1.y * v1point.x;
	    
	    //if all left turns
	    if (turn1 > 0 && turn2 > 0 && turn3 > 0)
		ret = true;
	}
	return ret;
	
    }
    
   
    private void finalizeMesh() {
	
	int i;
	
	//combine frontVerts and backVerts into one array
        //size is length * 6 because the number of vertices are tripled, to allow
            //realistic shading (for each vertex, we have 3 normals, because
            //each vertex contributes to 3 faces)
	Vector3f[] verts = new Vector3f[length * 6];
	for (i = 0; i < length; i++) {
	    verts[i] = frontVerts[i];
            verts[i + (length * 2)] = verts[i];
            verts[i + (length * 4)] = verts[i];
        }
	for(i = length; i < length * 2; i++) {
	    verts[i] = backVerts[i - length];
            verts[i + (length * 2)] = verts[i];
            verts[i + (length * 4)] = verts[i];
        }
	
	//get triangle indices from arraylist to array
	triangleIndicesArray = new int[triangleIndices.size()];
	for (i = 0; i < triangleIndices.size(); i++)
	    triangleIndicesArray[i] = (Integer) triangleIndices.get(i);
	
	setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(verts));
        setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(triangleIndicesArray));
	if(vertColor)
	    setBuffer(Type.Color, 4, colorArray);
	updateBound();
	
    }
    
}
