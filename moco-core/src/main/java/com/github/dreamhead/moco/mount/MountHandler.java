package com.github.dreamhead.moco.mount;

import com.github.dreamhead.moco.handler.AbstractContentResponseHandler;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.HttpRequest;

import java.io.File;
import java.io.IOException;

import static com.google.common.io.Files.toByteArray;

public class MountHandler extends AbstractContentResponseHandler {
    private final MountPathExtractor mountPathExtractor;

    private final File dir;

    public MountHandler(File dir, MountTo target) {
        this.dir = dir;
        this.mountPathExtractor = new MountPathExtractor(target);
    }

    @Override
    protected void writeContentResponse(HttpRequest request, ChannelBuffer buffer) {
        try {
            buffer.writeBytes(toByteArray(targetFile(request)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File targetFile(HttpRequest request) {
        String relativePath = mountPathExtractor.extract(request);
        return new File(dir, relativePath);
    }
}
