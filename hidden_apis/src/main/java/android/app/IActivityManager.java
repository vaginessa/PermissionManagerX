package android.app;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

public interface IActivityManager {

  int startActivityAsUser(
      IApplicationThread caller,
      String callingPackage,
      Intent intent,
      String resolvedType,
      IBinder resultTo,
      String resultWho,
      int requestCode,
      int startFlags,
      ProfilerInfo profilerInfo,
      Bundle bOptions,
      int userId)
      throws RemoteException;

  // R+
  ComponentName startService(
      IApplicationThread caller,
      Intent service,
      String resolvedType,
      boolean requireForeground,
      String callingPackage,
      String callingFeatureId,
      int userId)
      throws RemoteException;

  // O+
  @SuppressWarnings("UnusedDeclaration")
  ComponentName startService(
      IApplicationThread caller,
      Intent service,
      String resolvedType,
      boolean requireForeground,
      String callingPackage,
      int userId)
      throws RemoteException;

  @SuppressWarnings("UnusedDeclaration")
  ComponentName startService(
      IApplicationThread caller,
      Intent service,
      String resolvedType,
      String callingPackage,
      int userId)
      throws RemoteException;

  int checkPermission(String permission, int pid, int uid) throws RemoteException;

  abstract class Stub {

    @SuppressWarnings("UnusedDeclaration")
    public static IActivityManager asInterface(IBinder obj) {
      return null;
    }
  }
}

class IApplicationThread {}

class ProfilerInfo {}
