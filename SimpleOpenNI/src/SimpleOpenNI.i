# -----------------------------------------------------------------------------
# SimpleOpenNI 
# -----------------------------------------------------------------------------
# Processing Wrapper for the OpenNI/Kinect library
# prog:  Max Rheiner / Interaction Design / zhdk / http://iad.zhdk.ch/
# -----------------------------------------------------------------------------

%module(directors="1") SimpleOpenNI

%{
#include "ContextWrapper.h"
#include "NITE_Helpers.h"
%}

%include "arrays_java.i"
%include "cpointer.i"
%include "typemaps.i"
%include "carrays.i"

%apply int[] {int *};

# ----------------------------------------------------------------------------
# Xn


JAVA_ARRAYSOFCLASSES(XnVector3D)
JAVA_ARRAYSOFCLASSES(XnPoint3D)



typedef	bool				XnBool;
typedef	char				XnChar;
typedef	unsigned int		XnUInt32;
typedef	int					XnInt32;
typedef	float				XnFloat;
typedef XnUInt32			XnVHandle;
typedef XnUInt32			XnStatus;
//typedef uint64_t			unsigned long;


typedef struct XnVector3D
{
	XnFloat X;
	XnFloat Y;
	XnFloat Z;
} XnVector3D;

typedef struct XnPoint3D
{
	XnFloat X;
	XnFloat Y;
	XnFloat Z;
} XnPoint3D;
// typedef XnVector3D XnPoint3D;

typedef XnFloat  XnConfidence;

typedef struct XnSkeletonJointPosition
{
	XnVector3D		position;
	XnConfidence	fConfidence;
} XnSkeletonJointPosition;

typedef struct XnMatrix3X3
{
	XnFloat elements[9];
} XnMatrix3X3;

typedef struct XnSkeletonJointOrientation
{
	XnMatrix3X3		orientation;
	XnConfidence	fConfidence;
} XnSkeletonJointOrientation;

typedef struct XnVCircle
{
    XnPoint3D ptCenter;
    XnFloat fRadius;
}XnVCircle;

typedef struct XnBoundingBox3D
{
	XnPoint3D LeftBottomNear;
	XnPoint3D RightTopFar;
} XnBoundingBox3D;

%array_class(XnVector3D, XnVector3DArray);
%array_class(XnPoint3D, XnPoint3DArray);


# ----------------------------------------------------------------------------
# stl

%include "std_vector.i"


%template(IntVector)	std::vector<int>;
%template(Vector3D)		std::vector<XnVector3D>;
%template(Point3D)		std::vector<XnPoint3D>;



# ----------------------------------------------------------------------------
# ContextWrapper

namespace sOpenNI{

	/*
typedef XnPoint3D*	XnPoint3DArrayX;
JAVA_ARRAYSOFCLASSES(XnPoint3DArrayX)
%apply XnPoint3D[] {XnPoint3DArrayX};
*/

%constant int USERS_ALL				= 0;		

%constant int SKEL_PROFILE_NONE		= XN_SKEL_PROFILE_NONE;
%constant int SKEL_PROFILE_ALL		= XN_SKEL_PROFILE_ALL;
%constant int SKEL_PROFILE_UPPER	= XN_SKEL_PROFILE_UPPER;
%constant int SKEL_PROFILE_LOWER	= XN_SKEL_PROFILE_LOWER;
%constant int SKEL_PROFILE_HEAD_HANDS = XN_SKEL_PROFILE_HEAD_HANDS;

%constant int SKEL_HEAD				= XN_SKEL_HEAD;		
%constant int SKEL_NECK				= XN_SKEL_NECK;			
%constant int SKEL_TORSO			= XN_SKEL_TORSO;
%constant int SKEL_WAIST			= XN_SKEL_WAIST;		
									 
%constant int SKEL_LEFT_COLLAR		= XN_SKEL_LEFT_COLLAR;
%constant int SKEL_LEFT_SHOULDER	= XN_SKEL_LEFT_SHOULDER;	
%constant int SKEL_LEFT_ELBOW		= XN_SKEL_LEFT_ELBOW;		
%constant int SKEL_LEFT_WRIST		= XN_SKEL_LEFT_WRIST;		
%constant int SKEL_LEFT_HAND		= XN_SKEL_LEFT_HAND;	
%constant int SKEL_LEFT_FINGERTIP	= XN_SKEL_LEFT_FINGERTIP;
									 
%constant int SKEL_RIGHT_COLLAR		= XN_SKEL_RIGHT_COLLAR;	
%constant int SKEL_RIGHT_SHOULDER	= XN_SKEL_RIGHT_SHOULDER;	
%constant int SKEL_RIGHT_ELBOW		= XN_SKEL_RIGHT_ELBOW;		
%constant int SKEL_RIGHT_WRIST		= XN_SKEL_RIGHT_WRIST;		
%constant int SKEL_RIGHT_HAND		= XN_SKEL_RIGHT_HAND;		
%constant int SKEL_RIGHT_FINGERTIP	= XN_SKEL_RIGHT_FINGERTIP;	
									 
%constant int SKEL_LEFT_HIP			= XN_SKEL_LEFT_HIP;		
%constant int SKEL_LEFT_KNEE		= XN_SKEL_LEFT_KNEE;		
%constant int SKEL_LEFT_ANKLE		= XN_SKEL_LEFT_ANKLE;		
%constant int SKEL_LEFT_FOOT		= XN_SKEL_LEFT_FOOT;		
									 
%constant int SKEL_RIGHT_HIP		= XN_SKEL_RIGHT_HIP;		
%constant int SKEL_RIGHT_KNEE		= XN_SKEL_RIGHT_KNEE;		
%constant int SKEL_RIGHT_ANKLE		= XN_SKEL_RIGHT_ANKLE;		
%constant int SKEL_RIGHT_FOOT		= XN_SKEL_RIGHT_FOOT;		


%constant int NODE_NONE			= Node_None;		
%constant int NODE_DEPTH		= Node_Depth;		
%constant int NODE_IMAGE		= Node_Image;		
%constant int NODE_IR			= Node_Ir;		
%constant int NODE_SCENE		= Node_Scene;		
%constant int NODE_USER			= Node_User;		
%constant int NODE_HANDS		= Node_Hands;		
%constant int NODE_GESTURE		= Node_Gesture;		
%constant int NODE_RECORDER		= Node_Recorder;		
%constant int NODE_PLAYER		= Node_Player;		

%constant int CODEC_NONE			= XN_CODEC_NULL;		
%constant int CODEC_UNCOMPRESSED	= XN_CODEC_UNCOMPRESSED;		
%constant int CODEC_JPEG			= XN_CODEC_JPEG;		
%constant int CODEC_16Z				= XN_CODEC_16Z;		
%constant int CODEC_16Z_EMB_TABLES	= XN_CODEC_16Z_EMB_TABLES;		
%constant int CODEC_CODEC_8Z		= XN_CODEC_8Z;		

%constant int RECORD_MEDIUM_FILE	= XN_CODEC_NULL;		


%constant int IMG_MODE_DEFAULT	= DepthImgMode_Default;		
%constant int IMG_MODE_RGB_FADE	= DepthImgMode_RgbFade;		

%constant int RUN_MODE_DEFAULT			= RunMode_Default;		
%constant int RUN_MODE_SINGLE_THREADED	= RunMode_SingleThreaded;		
%constant int RUN_MODE_MULTI_THREADED	= RunMode_MultiThreaded;		



%feature("director") ContextWrapper;
class ContextWrapper
{
public:

	ContextWrapper();

	int version();

	bool init(const char* xmlInitFile,int runMode=RunMode_MultiThreaded);
	bool init(int runMode=RunMode_MultiThreaded);

	int nodes();

	void addLicense(const char* vendor,const char* license);
	
	bool isInit();
	void close();
	
	virtual bool enableDepth();
	virtual bool enableDepth(int width,int height,int fps);

	virtual bool enableRGB();
	virtual bool enableRGB(int width,int height,int fps);
	
	virtual bool enableIR();
	virtual bool enableIR(int width,int height,int fps);

	virtual bool enableScene();
	virtual bool enableScene(int width,int height,int fps);

	virtual bool enableUser(int flags);
	virtual bool enableHands();
	virtual bool enableGesture();

	virtual void update();

	int depthWidth();
	int	depthHeight();
	int	depthImage(int* map);	
	void setDepthImageColor(int r,int g,int b);
	void setDepthImageColorMode(int mode);
	int depthImageColorMode();
	
	int depthMapSize();
	int	depthMap(int* map);					
	int depthMapRealWorld(XnPoint3D map[]);	
	int depthMapRealWorldA(XnPoint3DArray* map);	
	//int depthMapRealWorldA(XnPoint3DArray map,int count);

	XnPoint3DArray* depthMapRealWorldA();		

	float hFieldOfView();
	float vFieldOfView();
	
	int rgbWidth();
	int rgbHeight();
	int	rgbImage(int* map);	
	
	int irWidth();
	int irHeight();
	int	irMap(int* map);	
	int	irImage(int* map);	

	int sceneWidth();
	int sceneHeight();
	int sceneMap(int* map);			
	int sceneImage(int* map);
	void getSceneFloor(XnVector3D* point,
					   XnVector3D* normal);
	
	int		userWidth();
	int		userHeight();

	bool	getCoM(int user, XnPoint3D&  com);
	int		getNumberOfUsers();
	int		getUsers(std::vector<int>* userList);
	int		getUserPixels(int user,int* userSceneData);

	bool	getUserPostition(int user, XnBoundingBox3D*  pPosition );

	
	bool	isCalibratedSkeleton(int user);
	bool	isCalibratingSkeleton(int user);
	void	requestCalibrationSkeleton(int user, bool force);
	void	abortCalibrationSkeleton(int user);
	
	bool	saveCalibrationDataSkeleton(int user,int slot);
	bool	loadCalibrationDataSkeleton(int user,int slot);
	void	clearCalibrationDataSkeleton(int slot);
	bool	isCalibrationDataSkeleton(int slot);

	void	setSmoothingSkeleton(float factor);
	
	bool	isTrackingSkeleton(int user);
	void	startTrackingSkeleton(int user);
	void	stopTrackingSkeleton(int user);

	void	startPoseDetection(const char* pose,int user);
	void	stopPoseDetection(int user);
	
	bool	getJointPositionSkeleton(int user,int joint,XnSkeletonJointPosition* jointPos);
	bool	getJointOrientationSkeleton(int user,
									    int joint,
										XnSkeletonJointOrientation* jointOrientation);
	
	
	void	startTrackingHands(const XnVector3D& pos);
	void	stopTrackingHands(int handId);
	void	stopTrackingAllHands();
	void	setSmoothingHands(float smoothingFactor);

	void addGesture(const char* gesture);
	void removeGesture(const char* gesture);
	bool availableGesture(const char *strGesture);
	
	virtual bool enableRecorder(int recordMedium,const char* filePath);
	bool addNodeToRecording(int nodeType,int compression);
	bool removeNodeFromRecording(int nodeType);	
	
	virtual bool openFileRecording(const char* filePath);
				   
	void setMirror(bool flag);
	bool mirror();
		
	void convertRealWorldToProjective(XnVector3D* world,XnVector3D* proj);	
	void convertRealWorldToProjective(std::vector<XnVector3D>* worldArray,std::vector<XnVector3D>* projArray);

	void convertProjectiveToRealWorld(XnVector3D* proj,XnVector3D* world);	
	void convertProjectiveToRealWorld(std::vector<XnVector3D>* projArray,std::vector<XnVector3D>* worldArray);

	///////////////////////////////////////////////////////////////////////////
	// XnVSessionMananger
	XnVSessionManager* createSessionManager(const XnChar* strUseAsFocus, const XnChar* strUseAsQuickRefocus,
											 xn::HandsGenerator*	pTracker = NULL, 
											 xn::GestureGenerator*	pFocusGenerator = NULL,
											 xn::GestureGenerator*	pQuickRefocusGenerator = NULL);

	void update(XnVSessionManager* sessionManager);



	///////////////////////////////////////////////////////////////////////////
	// time stamps

	unsigned long depthMapTimeStamp();
	unsigned long depthImageTimeStamp();
	unsigned long depthRealWorldTimeStamp();
	unsigned long imageTimeStamp();
	unsigned long irTimeStamp();
	unsigned long sceneTimeStamp();
	unsigned long userTimeStamp();
	unsigned long handsTimeStamp();

	unsigned long updateTimeStamp();
	unsigned long updateSubTimeStamp();

protected:

	virtual void onNewUserCb(unsigned int userId);
	virtual void onLostUserCb(unsigned int userId);
	
	virtual void onStartCalibrationCb(unsigned int userId);
	virtual void onEndCalibrationCb(unsigned int userId,bool successFlag);

	virtual void onStartPoseCb(const char* strPose, unsigned int user);
	virtual void onEndPoseCb(const char* strPose, unsigned int user);

	virtual void onCreateHandsCb(unsigned int nId, const XnPoint3D* pPosition, float fTime);
	virtual void onUpdateHandsCb(unsigned int nId, const XnPoint3D* pPosition, float fTime);
	virtual void onDestroyHandsCb(unsigned int nId, float fTime);

	virtual void onRecognizeGestureCb(const char* strGesture, const XnPoint3D* pIdPosition,const XnPoint3D* pEndPosition);
	virtual void onProgressGestureCb(const char* strGesture, const XnPoint3D* pPosition,float fProgress);

	virtual void onStartSessionCb(const XnPoint3D& ptPosition);
	virtual void onEndSessionCb();
	virtual void onFocusSessionCb(const XnChar* strFocus, const XnPoint3D& ptPosition, XnFloat fProgress);

};

};


# -----------------------------------------------------------------------------
# Context

namespace xn{

/*
class EnumerationErrors
{
public:
    EnumerationErrors();
    EnumerationErrors(XnEnumerationErrors* pErrors, XnBool bOwn = FALSE);
    
    ~EnumerationErrors();

    class Iterator
    {
    public:
        friend class EnumerationErrors;

        XnBool operator==(const Iterator& other) const;
        XnBool operator!=(const Iterator& other) const;
        Iterator& operator++();
        Iterator operator++(int);

        const XnProductionNodeDescription& Description();
        XnStatus Error();
		
		Iterator(XnEnumerationErrorsIterator it);
    };

    Iterator Begin() const;
    Iterator End() const;

    XnStatus ToString(XnChar* csBuffer, XnUInt32 nSize);
    void Free();
    XnEnumerationErrors* GetUnderlying();
};
*/

class Context
{
public:
    
    Context();
    Context(XnContext* pContext);
    Context(const Context& other);

    ~Context();

    XnContext* GetUnderlyingObject() const;

    XnStatus Init();

    /*
    XnStatus RunXmlScript(const XnChar* strScript, EnumerationErrors* pErrors = NULL);

    XnStatus RunXmlScriptFromFile(const XnChar* strFileName, EnumerationErrors* pErrors = NULL);
	
    XnStatus InitFromXmlFile(const XnChar* strFileName, EnumerationErrors* pErrors = NULL);
	*/

/*
    XnStatus OpenFileRecording(const XnChar* strFileName);

    XnStatus CreateMockNode(XnProductionNodeType type, const XnChar* strName, ProductionNode& node);

    XnStatus CreateMockNodeBasedOn(ProductionNode& originalNode, const XnChar* strName, ProductionNode& mockNode);

    XnStatus CreateCodec(XnCodecID codecID, ProductionNode& initializerNode, Codec& codec);

    void Shutdown();

    XnStatus AddLicense(const XnLicense& License);

    XnStatus EnumerateLicenses(XnLicense*& aLicenses, XnUInt32& nCount) const;

    static void FreeLicensesList(XnLicense aLicenses[]);

//    EnumerateProductionTrees(XnProductionNodeType Type, Query* pQuery, NodeInfoList& TreesList, EnumerationErrors* pErrors = NULL) const;
 
    XnStatus CreateAnyProductionTree(XnProductionNodeType type, Query* pQuery, ProductionNode& node, EnumerationErrors* pErrors = NULL);

    XnStatus CreateProductionTree(NodeInfo& Tree);

    XnStatus EnumerateExistingNodes(NodeInfoList& list) const;

    XnStatus EnumerateExistingNodes(NodeInfoList& list, XnProductionNodeType type) const;

    XnStatus FindExistingNode(XnProductionNodeType type, ProductionNode& node) const;

    XnStatus GetProductionNodeByName(const XnChar* strInstanceName, ProductionNode& node) const;

    XnStatus GetProductionNodeInfoByName(const XnChar* strInstanceName, NodeInfo& nodeInfo) const;
    
    XnStatus StartGeneratingAll();

    XnStatus StopGeneratingAll();

    XnStatus SetGlobalMirror(XnBool bMirror);

    XnBool GetGlobalMirror();

    XnStatus GetGlobalErrorState();

    XnStatus RegisterToErrorStateChange(XnErrorStateChangedHandler handler, void* pCookie, XnCallbackHandle& hCallback);

    void UnregisterFromErrorStateChange(XnCallbackHandle hCallback);

    XnStatus WaitAndUpdateAll();

    XnStatus WaitAnyUpdateAll();

    XnStatus WaitOneUpdateAll(ProductionNode& node);

    XnStatus WaitNoneUpdateAll();

    XnStatus AutoEnumerateOverSingleInput(NodeInfoList& List, XnProductionNodeDescription& description, const XnChar* strCreationInfo, XnProductionNodeType InputType, EnumerationErrors* pErrors, Query* pQuery = NULL) const;

    void SetHandle(XnContext* pContext);
    */
    
};

 
};


# ----------------------------------------------------------------------------
# NITE

%include "SimpleNite.i"