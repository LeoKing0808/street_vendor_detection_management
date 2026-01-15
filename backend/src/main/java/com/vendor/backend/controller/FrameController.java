package com.vendor.backend.controller;

import com.vendor.backend.dto.request.RequestDTOs;
import com.vendor.backend.dto.response.ResponseDTOs.ApiResponse;
import com.vendor.backend.entity.Frame;
import com.vendor.backend.service.FrameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/frames")
public class FrameController {
    @Autowired
    private FrameService frameService;

    /**
     * 批量创建帧信息
     * @param frames 帧信息列表
     * @return 创建的帧信息
     */
    @PostMapping
    public ApiResponse<List<Frame>> createFrames(@Valid @RequestBody List<RequestDTOs.CreateFrameRequest> frames) {
        // 转换请求体为Frame对象
        List<Frame> frameList = frames.stream().map(frameData -> {
            Frame frame = new Frame();
            frame.setCameraId(frameData.getCameraId());
            frame.setObjects(frameData.getObjects());
            frame.setImageUrl(frameData.getImageUrl());
            return frame;
        }).toList();

        List<Frame> savedFrames = frameService.saveFrames(frameList);
        return ApiResponse.success("帧信息创建成功", savedFrames);
    }
}