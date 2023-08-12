package com.fetchRewards;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fetchRewards.controller.ReceiptController;
import com.fetchRewards.dto.ProcessReceiptResponse;
import com.fetchRewards.pojo.Receipt;
import com.fetchRewards.service.ReceiptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class FetchRewardsApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ReceiptService receiptService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Autowired
	private ObjectMapper objectMapper; // Jackson ObjectMapper

	@Test
	public void testProcessReceipt_Success() throws Exception {

		String mockReceiptJson = "{\"retailer\": \"mockRetailer\", \"purchaseDate\" : \"2023-08-10\", \"purchaseTime\" : \"13:13\", \"total\" : \"1.25\", \"items\" : [{\"shortDescription\" : \"CanadaDry\", \"price\" : \"1.25\"}]}";
		String mockResponseJson = "{\"id\": \"mockId\"}";

		ProcessReceiptResponse mockResponse = objectMapper.readValue(mockResponseJson, ProcessReceiptResponse.class); // Deserialize response JSON


		when(receiptService.processReceipt(any(Receipt.class))).thenReturn(mockResponse);

		mockMvc.perform(MockMvcRequestBuilders
						.post("/receipts/process")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mockReceiptJson))
						.andExpect(MockMvcResultMatchers.status().isOk())
						.andExpect(MockMvcResultMatchers.content().json(mockResponseJson));
	}

	@Test
	public void testProcessReceipt_BadRequest() throws Exception {

		String mockReceiptJson = "{\"retailer\":  , \"purchaseDate\" : \"2023-08-10\", \"purchaseTime\" : \"13:13\", \"total\" : \"1.25\", \"items\" : [{\"shortDescription\" : \"CanadaDry\", \"price\" : \"1.25\"}]}";
		String mockResponseJson = "{\"id\": \"mockId\"}";

		ProcessReceiptResponse mockResponse = objectMapper.readValue(mockResponseJson, ProcessReceiptResponse.class); // Deserialize response JSON


		when(receiptService.processReceipt(any(Receipt.class))).thenReturn(mockResponse);

		mockMvc.perform(MockMvcRequestBuilders
						.post("/receipts/process")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mockReceiptJson))
						.andExpect(MockMvcResultMatchers.status().isBadRequest())
						.andExpect(MockMvcResultMatchers.content().string(""));
	}

	@Test
	public void testGetPointsForReceiptId_ReceiptId_Exists() throws Exception {
		int mockPoints = 100;

		String mockReceiptJson = "{\"retailer\": \"mockRetailer\", \"purchaseDate\" : \"2023-08-10\", \"purchaseTime\" : \"13:13\", \"total\" : \"1.25\", \"items\" : [{\"shortDescription\" : \"CanadaDry\", \"price\" : \"1.25\"}]}";
		Receipt mockRequest = objectMapper.readValue(mockReceiptJson, Receipt.class); // Deserialize request JSON

		when(receiptService.getReceiptById(anyString())).thenReturn(mockRequest);
		when(receiptService.getPointsForReceiptId(any(Receipt.class))).thenReturn(mockPoints);

		mockMvc.perform(MockMvcRequestBuilders
						.get("/receipts/{id}/points", "mockId")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.points").value(mockPoints));
	}

	@Test
	public void testGetPointsForReceiptId_ReceiptId_Does_Not_Exist() throws Exception {
		String mockId = "mockId";
		when(receiptService.getReceiptById(anyString())).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders
						.get("/receipts/{id}/points", mockId)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Receipt not found for ID: " + mockId));
	}



}
