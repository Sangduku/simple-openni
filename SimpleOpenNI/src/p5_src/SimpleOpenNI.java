/* ----------------------------------------------------------------------------
 * SimpleOpenNI
 * ----------------------------------------------------------------------------
 * Copyright (C) 2011 Max Rheiner / Interaction Design Zhdk
 *
 * This file is part of SimpleOpenNI.
 *
 * SimpleOpenNI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version (subject to the "Classpath" exception
 * as provided in the LICENSE.txt file that accompanied this code).
 *
 * SimpleOpenNI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SimpleOpenNI.  If not, see <http://www.gnu.org/licenses/>.
 * ----------------------------------------------------------------------------
 */
 
package SimpleOpenNI;

import processing.core.*;
import java.lang.reflect.Method;
   
public class SimpleOpenNI extends ContextWrapper implements SimpleOpenNIConstants
{
	static 
	{	// load the nativ shared lib
		System.loadLibrary("SimpleOpenNI");
	
	}
	///////////////////////////////////////////////////////////////////////////
	// callback vars
	protected Method 			_newUserMethod;
	protected Method 			_lostUserMethod;
	
	protected Method 			_startCalibrationMethod;
	protected Method 			_endCalibrationMethod;
	
	protected Method 			_startPoseMethod;
	protected Method 			_endPoseMethod;

	// hands cb
	protected Method 			_createHandsMethod;
	protected Method 			_updateHandsMethod;
	protected Method 			_destroyHandsMethod;
	
	// gesture cb
	protected Method 			_recognizeGestureMethod;
	protected Method 			_progressGestureMethod;

	
	
	protected String 			_filename;	
	protected PApplet			_parent;
	
	protected PImage			_depthImage;
	protected int[]				_depthRaw;
	protected PVector[]			_depthMapRealWorld;
	XnVector3D[] 				_depthMapRealWorldXn;
	
	protected PImage			_rgbImage;

	protected PImage			_irImage;
	
	protected PImage			_sceneImage;
  	protected int[]				_sceneRaw;

  	protected int[]				_userRaw;
	
	/**
	* Creates the OpenNI context ands inits the modules
	* 
	* @param parent
	*          PApplet
	* @param initXMLFile
	*          String
	* @return
	*/
	public SimpleOpenNI(PApplet parent, String initXMLFile)
	{
		this._parent 	= parent;
		parent.registerDispose(this);
		
		// setup the callbacks
		setupCallbackFunc();
		
		// load the initfile
		this.init(parent.dataPath(initXMLFile));
	}

	/**
	* Creates the OpenNI context ands inits the modules
	* 
	* @param parent
	*          PApplet
	* @return
	*/
	public SimpleOpenNI(PApplet parent)
	{
		this._parent 	= parent;
		parent.registerDispose(this);
		
		// setup the callbacks
		setupCallbackFunc();
		
		// load the initfile
		this.init();
	}
	
	protected void setupCallbackFunc()
	{
		this._newUserMethod				= null;
		this._lostUserMethod 			= null;
		
		this._startCalibrationMethod 	= null;
		this._endCalibrationMethod		= null;
		
		this._startPoseMethod 			= null;
		this._endPoseMethod				= null;

		this._createHandsMethod			= null;
		this._updateHandsMethod			= null;
		this._destroyHandsMethod		= null;
	
		// user callbacks
		_newUserMethod = getMethodRef("onNewUser",new Class[] { int.class });
		_lostUserMethod = getMethodRef("onLostUser",new Class[] { int.class });

		// calibrations callbacks
		_startCalibrationMethod = getMethodRef("onStartCalibration",new Class[] { int.class });
		_endCalibrationMethod = getMethodRef("onEndCalibration",new Class[] { int.class, boolean.class });
		
		// pose callbacks
		_startPoseMethod = getMethodRef("onStartPose",new Class[] { String.class,int.class });
		_endPoseMethod = getMethodRef("onEndPose",new Class[] { String.class,int.class });
		
		// hands
		_createHandsMethod = getMethodRef("onCreateHands",new Class[] { int.class,PVector.class,float.class });
		_updateHandsMethod = getMethodRef("onUpdateHands",new Class[] { int.class,PVector.class,float.class });
		_destroyHandsMethod = getMethodRef("onDestroyHands",new Class[] { int.class,float.class });

		// gesture
		_recognizeGestureMethod = getMethodRef("onRecognizeGesture",new Class[] { String.class,PVector.class,PVector.class  });
		_progressGestureMethod = getMethodRef("onProgressGesture",new Class[] { String.class,PVector.class,float.class });
	}
	
	protected Method getMethodRef(String methodName,Class[] paraList)
	{
		Method	ret = null;
		try {
			ret = _parent.getClass().getMethod(methodName,paraList);																									
		} 
		catch (Exception e) 
		{ // no such method, or an error.. which is fine, just ignore
		}
		return ret;
	}
	
	/**
	* 
	*/  
	public void dispose() 
	{
		close();
	}

	public void finalize() 
	{
		close();
	}

	private void setupDepth()
	{
		
		_depthImage 		= new PImage(depthWidth(), depthHeight(),PConstants.RGB);
		_depthRaw 			= new int[depthMapSize()];
		_depthMapRealWorld 	= new PVector[depthMapSize()];
		_depthMapRealWorldXn = new XnVector3D[depthMapSize()];
			
		for(int i=0;i < depthMapSize();i++ )
		{
			_depthMapRealWorld[i] 	= new PVector();
			_depthMapRealWorldXn[i] = new XnVector3D();
		}
	}
	
	/**
	* Enable the depthMap data collection
	*/  
	public boolean enableDepth() 
	{
		if(super.enableDepth())
		{	// setup the var for depth calc
			setupDepth();
			return true;
		}
		else
			return false;
	}
	
	/**
	* Enable the depthMap data collection
	* 
	* @param width
	*          int
	* @param height
	*          int
	* @param fps
	*          int
	* @return
	*/
	public boolean enableDepth(int width,int height,int fps) 
	{
		if(super.enableDepth(width,height,fps))
		{	// setup the var for depth calc
			setupDepth();
			return true;
		}
		else
			return false;
	}	
	
	public PImage depthImage() 
	{
		return _depthImage;
	}
	
	public int[] depthMap()
	{
		return _depthRaw;
	}

	public PVector[] depthMapRealWorld()
	{
		return _depthMapRealWorld;
	}	
	
	private void setupRGB()
	{
		_rgbImage = new PImage(rgbWidth(), rgbHeight(),PConstants.RGB);
	}
	
	/**
	* Enable the camera image collection
	*/  
	public boolean enableRGB() 
	{
		if(super.enableRGB())
		{	// setup the var for depth calc
			setupRGB();
			return true;
		}
		else
			return false;
	}	

	/**
	* Enable the camera image collection
	* 
	* @param width
	*          int
	* @param height
	*          int
	* @param fps
	*          int
	* @return
	*/
	public boolean enableRGB(int width,int height,int fps) 
	{
		if(super.enableRGB(width,height,fps))
		{	// setup the var for depth calc
			setupRGB();
			return true;
		}
		else
			return false;
	}	

	public PImage rgbImage() 
	{
		return _rgbImage;
	}
		
	private void setupIR()
	{
		_irImage = new PImage(irWidth(), irHeight(),PConstants.RGB);
	}
		
	/**
	* Enable the irMap data collection
	* ir is only available if there is no rgbImage activated at the same time 
	*/  
	public boolean enableIR() 
	{
		if(super.enableIR())
		{	// setup the var for depth calc
			setupIR();
			return true;
		}
		else
			return false;
	}
	
	/**
	* Enable the irMap data collection
	* ir is only available if there is no rgbImage activated at the same time 
	* 
	* @param width
	*          int
	* @param height
	*          int
	* @param fps
	*          int
	* @return
	*/
	public boolean enableIR(int width,int height,int fps) 
	{
		if(super.enableIR(width,height,fps))
		{	// setup the var for depth calc
			setupIR();
			return true;
		}
		else
			return false;
	}		
	
	public PImage irImage() 
	{
		return _irImage;
	}

	private void setupScene()
	{
		_sceneImage = new PImage(sceneWidth(), sceneHeight(),PConstants.RGB);
		_sceneRaw = new int[sceneWidth() * sceneHeight()];
	}
	
	/**
	* Enable the scene data collection
	*/  
	public boolean enableScene() 
	{
		if(super.enableScene())
		{	// setup the var for depth calc
			setupScene();
			return true;
		}
		else
			return false;
	}
	
	/**
	* Enable the scene data collection
	* 
	* @param width
	*          int
	* @param height
	*          int
	* @param fps
	*          int
	* @return
	*/
	public boolean enableScene(int width,int height,int fps) 
	{
		if(super.enableScene(width,height,fps))
		{	// setup the var for depth calc
			setupScene();
			return true;
		}
		else
			return false;
	}	
	
	public PImage sceneImage()
	{
		return _sceneImage;
	}
	
	public int[] sceneMap()
	{
		return _sceneRaw;
	}

	
	public void getSceneFloor(PVector point,PVector normal)
	{
		XnVector3D p = new XnVector3D();
		XnVector3D n = new XnVector3D();
		
		super.getSceneFloor(p, n);
		point.set(p.getX(),p.getY(),p.getZ());
		normal.set(n.getX(),n.getY(),n.getZ());
	}
	
	private void setupUser()
	{
		_userRaw = new int[userWidth() * userHeight()];
	}
	
	/**
	* Enable user 
	*/  
	public boolean enableUser(int flags) 
	{
		if(super.enableUser(flags))
		{
			setupUser();
			return true;
		}
		else
			return false;
	}
	
	public int[] getUsersPixels(int user)
	{
		int size = userWidth() * userHeight();
		if(size == 0)
			return _userRaw;
			
		if(_userRaw.length != userWidth() * userHeight())
		{	// resize the array
			_userRaw = new int[userWidth() * userHeight()];
		}

		super.getUserPixels(user,_userRaw);
		return _userRaw;
	}
		
	public boolean getCoM(int user,PVector com)
	{
		boolean ret;
		XnVector3D com1 = new XnVector3D();
		ret = super.getCoM(user,com1);	
		com.set(com1.getX(),
				com1.getY(),
				com1.getZ());
		
		return ret;
	}
	
	private void setupHands()
	{}
	
	/**
	* Enable hands  
	*/  
	public boolean enableHands() 
	{
		if(super.enableHands())
		{
			setupHands();
			return true;
		}
		else
			return false;
	}	

	public void	startTrackingHands(PVector pos)
	{
		XnVector3D vec = new XnVector3D();
		vec.setX(pos.x);
		vec.setY(pos.y);
		vec.setZ(pos.z);
		super.startTrackingHands(vec);
	}

	private void setupGesture()
	{}
	
	/**
	* Enable gesture  
	*/  
	public boolean enableGesture() 
	{
		if(super.enableGesture())
		{
			setupGesture();
			return true;
		}
		else
			return false;
	}	
	
	/**
	* Enable recorder	
	*/
	public boolean enableRecorder(int recordMedium,String filePath)
	{
		String path = _parent.dataPath(filePath);
		_parent.createPath(path);

		if(super.enableRecorder(recordMedium,path))
		{
			return true;
		}
		else
			return false;
	}
	
	/**
	* Enable the player
	*/  
	public boolean openFileRecording(String filePath)
	{
		String path = _parent.dataPath(filePath);
		
		if(super.openFileRecording(path))
		{	// get all the nodes that are in use and init them

			if((nodes() & NODE_DEPTH) > 0)
				setupDepth();
			if((nodes() & NODE_IMAGE) > 0)
				setupRGB();
			if((nodes() & NODE_IR) > 0)
				setupIR();
			if((nodes() & NODE_SCENE) > 0)
				setupScene();
			if((nodes() & NODE_USER) > 0)
				setupUser();
			if((nodes() & NODE_GESTURE) > 0)
				setupGesture();
			if((nodes() & NODE_HANDS) > 0)
				setupHands();
			
			return true;
		}
		else
			return false;
	}
	
	/**
	* Enable the user data collection
	*/  
	public void update() 
	{
		super.update();
		
		// copy the depth map
		if((nodes() & NODE_DEPTH) > 0)
		{
			_depthImage.loadPixels();
				depthImage(_depthImage.pixels);
			_depthImage.updatePixels();
			
			depthMap(_depthRaw);
			
			XnVector3D vec;
			depthMapRealWorld(_depthMapRealWorldXn);
			for(int i=0;i < _depthMapRealWorldXn.length;i++)
			{
				vec = _depthMapRealWorldXn[i];
				_depthMapRealWorld[i].set(vec.getX(),
										  vec.getY(),
									      vec.getZ());
			}
					
			/* still have to find out how i get an return array back with swig
			XnVector3D[] depthMapRealWorld = depthMapRealWorldA();
			for(int i=0;i < depthMapSize();i++)
			{
				_depthMapRealWorld[i].set(depthMapRealWorld[i].getX(),
										  depthMapRealWorld[i].getY(),
									      depthMapRealWorld[i].getZ());
			}
			*/
			
		}
		
		// copy the rgb map
		if((nodes() & NODE_IMAGE) > 0)
		{
			_rgbImage.loadPixels();
				rgbImage(_rgbImage.pixels);
			_rgbImage.updatePixels();
		}
		
		// copy the ir map
		if((nodes() & NODE_IR) > 0)
		{
			_irImage.loadPixels();
				irImage(_irImage.pixels);
			_irImage.updatePixels();
		}
		
		// copy the scene map
		if((nodes() & NODE_SCENE) > 0)
		{
			_sceneImage.loadPixels();
				sceneImage(_sceneImage.pixels);
			_sceneImage.updatePixels();
			
			sceneMap(_sceneRaw);
		}
			
		
	}	
	
	/**
	* Draws a limb from joint1 to joint2
	* 
	* @param userId
	*          int
	* @param joint1
	*          int
	* @param joint2
	*          int
	*/
	public void drawLimb(int userId, int joint1, int  joint2)
	{
		if (!isCalibratedSkeleton(userId))
			return;
		if (!isTrackingSkeleton(userId))
			return;

		XnSkeletonJointPosition joint1Pos = new XnSkeletonJointPosition();
		XnSkeletonJointPosition joint2Pos = new XnSkeletonJointPosition();
		
		getJointPositionSkeleton(userId, joint1, joint1Pos);
		getJointPositionSkeleton(userId, joint2, joint2Pos);

		if (joint1Pos.getFConfidence() < 0.5 || joint2Pos.getFConfidence() < 0.5)
			return;
			
		// calc the 3d coordinate to screen coordinates
		XnVector3D pt1 = new XnVector3D();
		XnVector3D pt2 = new XnVector3D();
		
		convertRealWorldToProjective(joint1Pos.getPosition(), pt1);
		convertRealWorldToProjective(joint2Pos.getPosition(), pt2);
		
		_parent.line(pt1.getX(), pt1.getY(),
					 pt2.getX(), pt2.getY());

	}

	/**
	* gets the coordinates of a joint
	* 
	* @param userId
	*          int
	* @param joint
	*          int
	* @param jointPos
	*          PVector
	* @return The confidence of this joint
	*          float
	*/	
	public float getJointPositionSkeleton(int userId,int joint,PVector jointPos)
	{
		if (!isCalibratedSkeleton(userId))
			return 0.0f;
		if (!isTrackingSkeleton(userId))
			return 0.0f;

		XnSkeletonJointPosition jointPos1 = new XnSkeletonJointPosition();
		
		getJointPositionSkeleton(userId, joint, jointPos1);
		jointPos.set(jointPos1.getPosition().getX(),
					 jointPos1.getPosition().getY(),
					 jointPos1.getPosition().getZ());

		return jointPos1.getFConfidence();
	}

	/**
	* gets the orientation of a joint
	* 
	* @param userId
	*          int
	* @param joint
	*          int
	* @param jointOrientation
	*          PMatrix3D
	* @return The confidence of this joint
	*          float
	*/	
	public float getJointOrientationSkeleton(int userId,int joint,PMatrix3D jointOrientation)
	{
		if (!isCalibratedSkeleton(userId))
			return 0.0f;
		if (!isTrackingSkeleton(userId))
			return 0.0f;

		XnSkeletonJointOrientation jointOrientation1 = new XnSkeletonJointOrientation();
		
		getJointOrientationSkeleton(userId, joint, jointOrientation1);
		
		// set the matrix by hand, openNI matrix is only 3*3(only rotation, no translation)
		float[] mat = jointOrientation1.getOrientation().getElements();
		jointOrientation.set(mat[0], mat[1], mat[2], 0,
							 mat[3], mat[4], mat[5], 0,
							 mat[6], mat[7], mat[8], 0,
							 0,		 0,		 0, 	 1);

		return jointOrientation1.getFConfidence();
	}

	
	
	public void convertRealWorldToProjective(PVector world,PVector proj) 
	{
		XnVector3D w = new XnVector3D();
		XnVector3D p = new XnVector3D();

		w.setX(world.x);
		w.setY(world.y);
		w.setZ(world.z);
		convertRealWorldToProjective(w,p);
		proj.set(p.getX(),
				 p.getY(),
				 p.getZ());
	}

	/*
	public void convertRealWorldToProjective(Vector3D worldArray, Vector3D projArray) 
	{
	}
	*/
	
	public void convertProjectiveToRealWorld(PVector proj, PVector world) 
	{
		XnVector3D p = new XnVector3D();
		XnVector3D w = new XnVector3D();

		p.setX( proj.x);
		p.setY( proj.y);
		p.setZ( proj.z);
		convertProjectiveToRealWorld(p,w);
		world.set(w.getX(),
				  w.getY(),
				  w.getZ());
	}

	/*
	public void convertProjectiveToRealWorld(Vector3D projArray, Vector3D worldArray) 
	{
	}
	*/
 
	///////////////////////////////////////////////////////////////////////////
	// helper methods
	public void drawCamFrustum()
	{
		_parent.g.pushStyle();
		
			// draw cam case
			_parent.stroke(200,200,0);  
			_parent.noFill();
			_parent.g.beginShape();
				_parent.g.vertex(270 * .5f,40 * .5f,0.0f);
				_parent.g.vertex(-270 * .5f,40 * .5f,0.0f);
				_parent.g.vertex(-270 * .5f,-40 * .5f,0.0f);
				_parent.g.vertex(270 * .5f,-40 * .5f,0.0f);
			_parent.g.endShape(PConstants.CLOSE);
			
			_parent.g.beginShape();
				_parent.g.vertex(220 * .5f,40 * .5f,-50.0f);
				_parent.g.vertex(-220 * .5f,40 * .5f,-50.0f);
				_parent.g.vertex(-220 * .5f,-40 * .5f,-50.0f);
				_parent.g.vertex(220 * .5f,-40 * .5f,-50.0f);
			_parent.g.endShape(PConstants.CLOSE);
			
			_parent.g.beginShape(PConstants.LINES);
				_parent.g.vertex(270 * .5f,40 * .5f,0.0f);
				_parent.g.vertex(220 * .5f,40 * .5f,-50.0f);
				
				_parent.g.vertex(-270 * .5f,40 * .5f,0.0f);
				_parent.g.vertex(-220 * .5f,40 * .5f,-50.0f);
				
				_parent.g.vertex(-270 * .5f,-40 * .5f,0.0f);
				_parent.g.vertex(-220 * .5f,-40 * .5f,-50.0f);
				
				_parent.g.vertex(270 * .5f,-40 * .5f,0.0f);
				_parent.g.vertex(220 * .5f,-40 * .5f,-50.0f);
			_parent.g.endShape();
			
			// draw cam opening angles
			_parent.stroke(200,200,0,50);  
			_parent.g.line(0.0f,0.0f,0.0f,
						   0.0f,0.0f,1000.0f);
			
			// calculate the angles of the cam, values are in radians, radius is 10m
			float distDepth = 10000;
			
			float valueH = distDepth * _parent.tan(hFieldOfView() * .5f); 
			float valueV = distDepth * _parent.tan(vFieldOfView() * .5f);       
			
			_parent.stroke(200,200,0,100);  
			_parent.g.line(0.0f,0.0f,0.0f,
						 valueH,valueV,distDepth);
			_parent.g.line(0.0f,0.0f,0.0f,
						 -valueH,valueV,distDepth);
			_parent.g.line(0.0f,0.0f,0.0f,
						 valueH,-valueV,distDepth);
			_parent.g.line(0.0f,0.0f,0.0f,
						 -valueH,-valueV,distDepth);
			_parent.g.beginShape();
				_parent.g.vertex(valueH,valueV,distDepth);
				_parent.g.vertex(-valueH,valueV,distDepth);
				_parent.g.vertex(-valueH,-valueV,distDepth);
				_parent.g.vertex(valueH,-valueV,distDepth);
			_parent.g.endShape(PConstants.CLOSE);
		
		_parent.g.popStyle();	
	}
	
	///////////////////////////////////////////////////////////////////////////
	// callbacks
	protected void onNewUserCb(long userId) 
	{
		try {
			_newUserMethod.invoke(_parent, new Object[] { (int)userId });
		} 
		catch (Exception e) 
		{
		}	
	}

	protected void onLostUserCb(long userId)
	{
		try {
			_lostUserMethod.invoke(_parent, new Object[] { (int)userId });		
		} 
		catch (Exception e) 
		{
		}	
	}

	protected void onStartCalibrationCb(long userId) 
	{
		try {
			_startCalibrationMethod.invoke(_parent, new Object[] { (int)userId });	
		} 
		catch (Exception e) 
		{
		}	
	}

	protected void onEndCalibrationCb(long userId, boolean successFlag) 
	{
		try {
			_endCalibrationMethod.invoke(_parent, new Object[] { (int)userId, successFlag});
		} 
		catch (Exception e) 
		{
		}	
	}

	protected void onStartPoseCb(String strPose, long userId) 
	{
		try {
			_startPoseMethod.invoke(_parent, new Object[] { strPose,(int)userId });
		} 
		catch (Exception e) 
		{
		}	
	}

	protected void onEndPoseCb(String strPose, long userId)
	{
		try {
			_endPoseMethod.invoke(_parent, new Object[] { strPose,(int)userId });
		} 
		catch (Exception e) 
		{
		}	
	}

	// hands
	protected void onCreateHandsCb(long nId, XnVector3D pPosition, float fTime)
	{
		try {
			_createHandsMethod.invoke(_parent, new Object[] { (int)nId,new PVector(pPosition.getX(),pPosition.getY(),pPosition.getZ()),fTime});
		} 
		catch (Exception e) 
		{}	
	}	
	
	protected void onUpdateHandsCb(long nId, XnVector3D pPosition, float fTime)
	{
		try {
			_updateHandsMethod.invoke(_parent, new Object[] { (int)nId,new PVector(pPosition.getX(),pPosition.getY(),pPosition.getZ()),fTime});
		} 
		catch (Exception e) 
		{}	
	}	
	
	protected void onDestroyHandsCb(long nId, float fTime)
	{
		try {
			_destroyHandsMethod.invoke(_parent, new Object[] { (int)nId,fTime});
		} 
		catch (Exception e) 
		{}	
	}	
  
	protected void onRecognizeGestureCb(String strGesture, XnVector3D pIdPosition, XnVector3D pEndPosition) 
	{
		try {
			_recognizeGestureMethod.invoke(_parent, new Object[] { strGesture,
																   new PVector(pIdPosition.getX(),pIdPosition.getY(),pIdPosition.getZ()),
																   new PVector(pEndPosition.getX(),pEndPosition.getY(),pEndPosition.getZ())
																 });
		} 
		catch (Exception e) 
		{}	
	}

	protected void onProgressGestureCb(String strGesture, XnVector3D pPosition, float fProgress) 
	{
		try {
			_progressGestureMethod.invoke(_parent, new Object[] { strGesture,
																  new PVector(pPosition.getX(),pPosition.getY(),pPosition.getZ()),
																  fProgress
																 });
		} 
		catch (Exception e) 
		{}	
	}	
}

