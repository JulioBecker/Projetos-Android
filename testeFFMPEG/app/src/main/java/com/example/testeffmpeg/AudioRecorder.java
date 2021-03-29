package com.example.testeffmpeg;

import android.media.MediaRecorder;

import java.io.IOException;

/**
 * Created by christian on 19/06/17.
 */

public class AudioRecorder {

    private MediaRecorder mRecorder;

    public AudioRecorder() {
    }

    public void start(String nomeArquivo) {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setAudioEncodingBitRate(24000);
        //mRecorder.setAudioEncodingBitRate(96000);
        mRecorder.setAudioSamplingRate(8000);
        //mRecorder.setAudioSamplingRate(44100);
        //mRecorder.setAudioEncodingBitRate(48000);
        //mRecorder.setAudioSamplingRate(16000);
        mRecorder.setOutputFile(MainActivity.getDiretorioPadrao() + nomeArquivo + ".3gp");

        try {
            mRecorder.prepare();
            mRecorder.start();
            CrashLog.d("Voice Recorder","started recording ");
        } catch (IOException e) {
            CrashLog.e("Voice Recorder", "prepare() failed "+e.getMessage());
        }
    }

    public void stop() {
        if (mRecorder == null)
            return;

        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

}
