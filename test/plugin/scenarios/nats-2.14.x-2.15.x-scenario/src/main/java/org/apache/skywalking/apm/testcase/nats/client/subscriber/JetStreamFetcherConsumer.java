/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.apache.skywalking.apm.testcase.nats.client.subscriber;

import io.nats.client.Connection;
import io.nats.client.JetStream;
import io.nats.client.JetStreamSubscription;
import io.nats.client.Message;
import io.nats.client.PullSubscribeOptions;
import io.nats.client.api.ConsumerConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.testcase.nats.client.work.StreamUtil;

import java.time.Duration;
import java.util.List;

@Slf4j
public class JetStreamFetcherConsumer implements Consumer {

    private final String stream;

    public JetStreamFetcherConsumer(String stream) {
        this.stream = stream;
    }

    @Override
    public void subscribe(Connection connection, String subject) {
        new Thread(() -> {
            try {
                ConsumerConfiguration cc = ConsumerConfiguration.builder()
                        .ackWait(Duration.ofMillis(100))
                        .build();
                PullSubscribeOptions pullOptions = PullSubscribeOptions.builder()
                        .durable(stream + "-durable")
                        .configuration(cc)
                        .build();
                StreamUtil.initStream(connection, subject, stream);
                JetStream js = connection.jetStream();
                JetStreamSubscription subscribe = js.subscribe(subject, pullOptions);
                List<Message> messages = subscribe.fetch(1, Duration.ofHours(1));
                if (messages != null) {
                    messages.forEach(msg -> log.info("received message : {} ", msg));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

}
