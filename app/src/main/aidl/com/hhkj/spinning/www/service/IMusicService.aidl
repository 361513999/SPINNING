// IMusicService.aidl
package com.hhkj.spinning.www.service;

// Declare any non-default types here with import statements

interface IMusicService {
     void play(int position,int index,int total);
     String isPlay();
     void stop();
     boolean status();
}
