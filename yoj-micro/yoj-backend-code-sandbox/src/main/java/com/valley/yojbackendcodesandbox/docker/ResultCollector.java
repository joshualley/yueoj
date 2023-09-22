package com.valley.yojbackendcodesandbox.docker;

import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * Docker容器执行结果收集器
 */
@Slf4j
@Data
public class ResultCollector extends ExecStartResultCallback {

    private String output = "";
    private String error = "";
    /**
     * 程序执行完成
     */
    private boolean finish = false;

    @Override
    public void onNext(Frame frame) {
        // 获取输出结果
        StreamType streamType = frame.getStreamType();
        String output = new String(frame.getPayload());
        switch (streamType) {
            case STDIN:
                break;
            case STDOUT:
                log.info("输出结果：" + output);
                this.output = output.trim();
                break;
            case STDERR:
                log.info("错误输出：" + output);
                this.error = output;
                break;
        }
        super.onNext(frame);
    }
    @Override
    public void onComplete() {
        finish = true;
        super.onComplete();
    }
}
