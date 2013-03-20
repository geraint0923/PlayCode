/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\GitHubRepo\\PrivateCode\\WifiAppTest\\src\\com\\example\\wifiapptest\\ITestService.aidl
 */
package com.example.wifiapptest;
public interface ITestService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.example.wifiapptest.ITestService
{
private static final java.lang.String DESCRIPTOR = "com.example.wifiapptest.ITestService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.example.wifiapptest.ITestService interface,
 * generating a proxy if needed.
 */
public static com.example.wifiapptest.ITestService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.example.wifiapptest.ITestService))) {
return ((com.example.wifiapptest.ITestService)iin);
}
return new com.example.wifiapptest.ITestService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_tick:
{
data.enforceInterface(DESCRIPTOR);
this.tick();
reply.writeNoException();
return true;
}
case TRANSACTION_startTriggerTester:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
this.startTriggerTester(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_startBatchTester:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
this.startBatchTester(_arg0, _arg1);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.example.wifiapptest.ITestService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void tick() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_tick, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void startTriggerTester(int count, int para) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(count);
_data.writeInt(para);
mRemote.transact(Stub.TRANSACTION_startTriggerTester, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void startBatchTester(int count, int para) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(count);
_data.writeInt(para);
mRemote.transact(Stub.TRANSACTION_startBatchTester, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_tick = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_startTriggerTester = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_startBatchTester = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public void tick() throws android.os.RemoteException;
public void startTriggerTester(int count, int para) throws android.os.RemoteException;
public void startBatchTester(int count, int para) throws android.os.RemoteException;
}
