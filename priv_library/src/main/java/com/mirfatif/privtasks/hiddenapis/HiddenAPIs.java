package com.mirfatif.privtasks.hiddenapis;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.AppOpsManager.OpEntry;
import android.app.AppOpsManager.PackageOps;
import android.app.IActivityManager;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.content.pm.ParceledListSlice;
import android.os.Build;
import android.os.Process;
import android.os.ServiceManager;
import android.permission.IPermissionManager;
import com.android.internal.app.IAppOpsService;
import com.mirfatif.annotation.DaemonOnly;
import com.mirfatif.annotation.HiddenClass;
import com.mirfatif.annotation.HiddenClass.CType;
import com.mirfatif.annotation.HiddenField;
import com.mirfatif.annotation.HiddenField.FType;
import com.mirfatif.annotation.HiddenMethod;
import com.mirfatif.annotation.HiddenMethod.MType;
import com.mirfatif.annotation.NonDaemonOnly;
import com.mirfatif.annotation.Privileged;
import com.mirfatif.annotation.Throws;
import com.mirfatif.privtasks.ser.MyPackageOps;
import java.util.List;

public abstract class HiddenAPIs {

  final HiddenAPIsCallback mCallback;
  Integer OP_FLAGS_ALL = null;

  @HiddenClass(cls = ServiceManager.class)
  @HiddenClass(cls = IAppOpsService.class)
  @HiddenClass(cls = IPackageManager.class)
  @HiddenClass(cls = IPermissionManager.class)
  @HiddenClass(cls = IActivityManager.class)
  @HiddenMethod(name = "getService", type = MType.STATIC_METHOD, cls = ServiceManager.class)
  @HiddenMethod(
      name = "asInterface",
      type = MType.STATIC_METHOD,
      cls = {
        IAppOpsService.Stub.class,
        IPackageManager.Stub.class,
        IPermissionManager.Stub.class,
        IActivityManager.Stub.class,
      })
  // IPackageManager and IPermissionManager don't have a constant in Context class
  HiddenAPIs(HiddenAPIsCallback callback) {
    mCallback = callback;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      try {
        OP_FLAGS_ALL = getOpFlagAll();
      } catch (HiddenAPIsError e) {
        e.printStackTrace();
      }
    }
  }

  //////////////////////////////////////////////////////////////////
  //////////////////////////// APP OPS /////////////////////////////
  //////////////////////////////////////////////////////////////////

  @HiddenField(name = "_NUM_OP", type = FType.STATIC_FIELD, cls = AppOpsManager.class)
  public int getNumOps() throws HiddenAPIsError {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      return _getNumOps();
    }
    // Using directly the value in compile-time SDK gets hard-coded
    return getStaticIntField("_NUM_OP", AppOpsManager.class);
  }

  @HiddenMethod(name = "getNumOps", type = MType.STATIC_METHOD, cls = AppOpsManager.class)
  abstract int _getNumOps() throws HiddenAPIsError;

  @HiddenField(name = "OP_NONE", type = FType.STATIC_FIELD, cls = AppOpsManager.class)
  public int getOpNone() throws HiddenAPIsError {
    // Using directly the value in compile-time SDK gets hard-coded
    return getStaticIntField("OP_NONE", AppOpsManager.class);
  }

  @HiddenField(
      name = "OP_FLAGS_ALL",
      type = FType.STATIC_FIELD,
      cls = AppOpsManager.class,
      minSDK = 29)
  public int getOpFlagAll() throws HiddenAPIsError {
    // Using directly the value in compile-time SDK gets hard-coded
    return getStaticIntField("OP_FLAGS_ALL", AppOpsManager.class);
  }

  @HiddenField(name = "MODE_NAMES", type = FType.STATIC_FIELD, cls = AppOpsManager.class)
  public abstract int getOpModeNamesSize() throws HiddenAPIsError;

  @HiddenMethod(name = "opToDefaultMode", type = MType.STATIC_METHOD, cls = AppOpsManager.class)
  public abstract int opToDefaultMode(int opCode, boolean isLos)
      throws HiddenAPIsError, HiddenAPIsException;

  @HiddenMethod(name = "opToSwitch", type = MType.STATIC_METHOD, cls = AppOpsManager.class)
  public abstract int opToSwitch(int opCode) throws HiddenAPIsError, HiddenAPIsException;

  @HiddenMethod(name = "opToName", type = MType.STATIC_METHOD, cls = AppOpsManager.class)
  public abstract String opToName(int opCode) throws HiddenAPIsError, HiddenAPIsException;

  @HiddenMethod(name = "modeToName", type = MType.STATIC_METHOD, cls = AppOpsManager.class)
  public abstract String modeToName(int opMode) throws HiddenAPIsError;

  @HiddenMethod(name = "permissionToOpCode", type = MType.STATIC_METHOD, cls = AppOpsManager.class)
  public abstract int permissionToOpCode(String permName);

  @DaemonOnly
  @HiddenMethod(name = "strDebugOpToOp", type = MType.STATIC_METHOD, cls = AppOpsManager.class)
  public abstract int strDebugOpToOp(String opName) throws HiddenAPIsError;

  @HiddenMethod(name = "setMode", cls = IAppOpsService.class)
  @DaemonOnly
  @Privileged(requires = "android.permission.MANAGE_APP_OPS_MODES")
  @Throws(name = "SecurityException")
  // Profile owners are allowed to change modes but only for apps within their user.
  public abstract void setMode(int op, int uid, String pkgName, int mode)
      throws HiddenAPIsException;

  @HiddenMethod(name = "setUidMode", cls = IAppOpsService.class)
  @DaemonOnly
  @Privileged(requires = "android.permission.MANAGE_APP_OPS_MODES")
  @Throws(name = "SecurityException")
  // Profile owners are allowed to change modes but only for apps within their user.
  public abstract void setUidMode(int op, int uid, int mode) throws HiddenAPIsException;

  @HiddenMethod(name = "resetAllModes", cls = IAppOpsService.class)
  @DaemonOnly
  @Privileged(requires = "android.permission.MANAGE_APP_OPS_MODES")
  @Throws(name = "SecurityException")
  // Profile owners are allowed to change modes but only for apps within their user.
  public abstract void resetAllModes(int userId, String pkgName) throws HiddenAPIsException;

  @HiddenClass(cls = PackageOps.class, type = CType.INNER_CLASS)
  @HiddenClass(cls = OpEntry.class, type = CType.INNER_CLASS)
  @HiddenMethod(name = "getOpsForPackage", cls = IAppOpsService.class)
  @HiddenMethod(name = "getUidOps", cls = IAppOpsService.class)
  @HiddenMethod(name = "getPackageName", cls = PackageOps.class)
  @HiddenMethod(name = "getOps", cls = PackageOps.class)
  @HiddenMethod(name = "getOp", cls = OpEntry.class)
  @HiddenMethod(name = "getMode", cls = OpEntry.class)
  @HiddenMethod(name = "getLastAccessTime", cls = OpEntry.class)
  @HiddenMethod(name = "getTime", cls = OpEntry.class)
  @Privileged(requires = "android.permission.GET_APP_OPS_STATS")
  @Throws(name = "SecurityException")
  /*
   getUidOps() (on O and P) is buggy, throws NullPointerException. Check was added in Q:
     android-10.0.0_r1: frameworks/base/services/core/java/com/android/server/appop/AppOpsService.java#1016
   But don't consider it an error, it just means there are no UID AppOps for the package.
   MIUI has bug and returns bad opCode like 10005, so compare with valid range.
   N and O (P too?) don't have getLastAccessTime(), so use deprecated getTime().
   Returning null is considered an error, so return empty List if no error.
  */
  public abstract List<MyPackageOps> getMyPackageOpsList(
      int uid, String packageName, String op, int opNum)
      throws HiddenAPIsException, HiddenAPIsError;

  //////////////////////////////////////////////////////////////////
  ////////////////////// MANIFEST PERMISSIONS //////////////////////
  //////////////////////////////////////////////////////////////////

  @HiddenField(
      name = "FLAG_PERMISSION_SYSTEM_FIXED",
      type = FType.STATIC_FIELD,
      cls = PackageManager.class)
  public static int getSystemFixedFlag() throws HiddenAPIsError {
    return getStaticIntField("FLAG_PERMISSION_SYSTEM_FIXED", PackageManager.class);
  }

  @HiddenField(
      name = "FLAG_PERMISSION_POLICY_FIXED",
      type = FType.STATIC_FIELD,
      cls = PackageManager.class)
  public static int getPolicyFixedFlag() throws HiddenAPIsError {
    return getStaticIntField("FLAG_PERMISSION_POLICY_FIXED", PackageManager.class);
  }

  @HiddenClass(cls = ParceledListSlice.class)
  @HiddenMethod(
      name = "getAllPermissionGroups",
      cls = {IPackageManager.class, IPermissionManager.class})
  @HiddenMethod(name = "getList", cls = ParceledListSlice.class)
  // getAllPermissionGroups() moved from IPackageManager to IPermissionManager in SDK 30.
  public abstract List<?> getPermGroupInfoList() throws HiddenAPIsException, HiddenAPIsError;

  @HiddenClass(cls = ParceledListSlice.class)
  @HiddenMethod(
      name = "queryPermissionsByGroup",
      cls = {IPackageManager.class, IPermissionManager.class})
  @HiddenMethod(name = "getList", cls = ParceledListSlice.class)
  // queryPermissionsByGroup() moved from IPackageManager to IPermissionManager in SDK 30.
  public abstract List<?> getPermInfoList(String permGroup)
      throws HiddenAPIsException, HiddenAPIsError;

  @HiddenMethod(
      name = "getPermissionFlags",
      cls = {IPackageManager.class, IPermissionManager.class})
  @DaemonOnly
  @Privileged(
      requires = {
        "android.permission.GRANT_RUNTIME_PERMISSIONS",
        "android.permission.REVOKE_RUNTIME_PERMISSIONS"
      })
  @Throws(name = "SecurityException")
  // getPermissionFlags() moved from IPackageManager to IPermissionManager in SDK 30.
  public abstract int getPermissionFlags(String permName, String pkgName, int userId)
      throws HiddenAPIsException;

  @HiddenMethod(name = "grantRuntimePermission", cls = IPackageManager.class)
  @DaemonOnly
  @Privileged(requires = "android.permission.GRANT_RUNTIME_PERMISSIONS")
  @Throws(name = "SecurityException")
  public abstract void grantRuntimePermission(String pkgName, String permName, int userId)
      throws HiddenAPIsException;

  @HiddenMethod(
      name = "revokeRuntimePermission",
      cls = {IPackageManager.class, IPermissionManager.class})
  @DaemonOnly
  @Privileged(requires = "android.permission.REVOKE_RUNTIME_PERMISSIONS")
  @Throws(name = "SecurityException")
  // revokeRuntimePermission() moved from IPackageManager to IPermissionManager in SDK 30.
  public abstract void revokeRuntimePermission(String pkgName, String permName, int userId)
      throws HiddenAPIsException;

  @HiddenMethod(
      name = "checkPermission",
      cls = {IActivityManager.class})
  public abstract int checkPermission(String perm, int pid, int uid) throws HiddenAPIsException;

  //////////////////////////////////////////////////////////////////
  //////////////////////////// PACKAGES ////////////////////////////
  //////////////////////////////////////////////////////////////////

  @HiddenMethod(name = "setApplicationEnabledSetting", cls = IPackageManager.class)
  @DaemonOnly
  @Privileged(
      requires = {
        "android.permission.CHANGE_COMPONENT_ENABLED_STATE",
        "android.permission.INTERACT_ACROSS_USERS"
      })
  @Throws(name = "SecurityException")
  public abstract void setApplicationEnabledSetting(
      String pkg, int state, int flags, int userId, String callingPkg) throws HiddenAPIsException;

  //////////////////////////////////////////////////////////////////
  ////////////////////////////// OTHERS ////////////////////////////
  //////////////////////////////////////////////////////////////////

  @HiddenMethod(name = "getPidsForCommands", type = MType.STATIC_METHOD, cls = Process.class)
  @DaemonOnly
  public abstract int[] getPidsForCommands(String[] commands);

  @HiddenMethod(name = "startActivityAsUser", cls = IActivityManager.class)
  @DaemonOnly
  @Privileged
  @Throws(name = "SecurityException")
  public abstract int openAppInfo(String pkgName, int userId) throws HiddenAPIsException;

  @HiddenField(name = "START_SUCCESS", type = FType.STATIC_FIELD, cls = ActivityManager.class)
  @DaemonOnly
  public int getAmSuccessCode() throws HiddenAPIsError {
    return getStaticIntField("START_SUCCESS", ActivityManager.class);
  }

  @HiddenMethod(
      name =
          "ComponentName startService(IApplicationThread, Intent, String, boolean, String, String, int)",
      cls = IActivityManager.class,
      minSDK = 30)
  @HiddenMethod(
      name = "ComponentName startService(IApplicationThread, Intent, String, boolean, String, int)",
      cls = IActivityManager.class,
      minSDK = 26,
      maxSDK = 29)
  @HiddenMethod(
      name = "ComponentName startService(IApplicationThread, Intent, String, String, int)",
      cls = IActivityManager.class,
      maxSDK = 25)
  @DaemonOnly
  @Privileged
  @Throws(name = "SecurityException")
  public abstract void sendRequest(String command, String appId, int userId, String codeWord)
      throws HiddenAPIsException;

  //////////////////////////////////////////////////////////////////
  ///////////////////////// COMMON METHODS /////////////////////////
  //////////////////////////////////////////////////////////////////

  public static int getStaticIntField(String name, Class<?> cls) throws HiddenAPIsError {
    try {
      return cls.getDeclaredField(name).getInt(null);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      throw new HiddenAPIsError(e);
    }
  }

  @NonDaemonOnly
  public abstract boolean canUseIAppOpsService();

  @NonDaemonOnly
  public abstract boolean canUseIPm();

  public interface HiddenAPIsCallback {

    void onGetUidOpsNpException(Exception e);

    void onInvalidOpCode(int opCode, String pkgName);
  }
}
