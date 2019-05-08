/*
 * Copyright 2019 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.pinpoint.profiler.context.provider.grpc;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.protobuf.GeneratedMessageV3;
import com.navercorp.pinpoint.bootstrap.config.GrpcTransportConfig;
import com.navercorp.pinpoint.common.util.Assert;
import com.navercorp.pinpoint.grpc.AgentHeaderFactory;
import com.navercorp.pinpoint.grpc.HeaderFactory;
import com.navercorp.pinpoint.profiler.context.module.MetadataConverter;
import com.navercorp.pinpoint.profiler.context.thrift.MessageConverter;
import com.navercorp.pinpoint.profiler.sender.EnhancedDataSender;
import com.navercorp.pinpoint.profiler.sender.grpc.AgentGrpcDataSender;
import io.grpc.NameResolverProvider;


/**
 * @author jaehong.kim
 */
public class AgentGrpcDataSenderProvider implements Provider<EnhancedDataSender<Object>> {
    private final GrpcTransportConfig grpcTransportConfig;
    private final MessageConverter<GeneratedMessageV3> messageConverter;
    private final HeaderFactory headerFactory;
    private final NameResolverProvider nameResolverProvider;

    @Inject
    public AgentGrpcDataSenderProvider(GrpcTransportConfig grpcTransportConfig,
                                       @MetadataConverter MessageConverter<GeneratedMessageV3> messageConverter,
                                       HeaderFactory headerFactory,
                                       NameResolverProvider nameResolverProvider) {
        this.grpcTransportConfig = Assert.requireNonNull(grpcTransportConfig, "grpcTransportConfig must not be null");
        this.messageConverter = Assert.requireNonNull(messageConverter, "messageConverter must not be null");
        this.headerFactory = Assert.requireNonNull(headerFactory, "headerFactory must not be null");
        this.nameResolverProvider = Assert.requireNonNull(nameResolverProvider, "nameResolverProvider must not be null");
    }

    @Override
    public EnhancedDataSender get() {
        String collectorTcpServerIp = grpcTransportConfig.getCollectorAgentServerIp();
        int collectorTcpServerPort = grpcTransportConfig.getCollectorAgentServerPort();

        return new AgentGrpcDataSender("Default", collectorTcpServerIp, collectorTcpServerPort,  messageConverter, headerFactory, nameResolverProvider);
    }

}