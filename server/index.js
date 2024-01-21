import express, { json } from "express";
import * as dotenv from "dotenv";
import cors from "cors";
import axios from "axios";
import { GoogleGenerativeAI } from "@google/generative-ai";
import * as fs from "fs";
import { pdfToPng } from "pdf-to-png-converter";
import multer from "multer";

dotenv.config();

const app = express();
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

const upload = multer();

const genAI = new GoogleGenerativeAI(process.env.GEMINI_API_KEY);
const model = genAI.getGenerativeModel({ model: "gemini-pro-vision" });

const fileToGenerativePart = (path, mimeType) => {
  return {
    inlineData: {
      data: Buffer.from(fs.readFileSync(path)).toString("base64"),
      mimeType,
    },
  };
};

app.get("/", (req, res) => {
  res.send("Hello World!");
});

app.post("/", upload.array("image", 16), async (req, res) => {
  // USING GEMINI PRO FOR TEXT ONLY
  // console.log(req.body);
  // const result = await model.generateContent(req.body.question);
  // const response = await result.response;
  // const text = response.text();
  // console.log(text);
  // res.json(text);

  // TESTING OUT GEMINI PRO VISION FOR IMAGE PROMPTS AS WELL
  if (!req.files) {
    return res.status(400).send("No files uploaded.");
  }

  const imageParts = req.files?.map((image) => ({
    inlineData: {
      data: image.buffer.toString("base64"),
      mimeType: "image/png",
    },
  }));

  const prompt =
    "I am studying for a quiz. This are pictures of my notes. Using these pictures and only information available in these pictures, help me to generate 10 questions and answers to test my knowledge. It should be in the format Question: ... Answer: ... The questions asked should be an MCQ format with 4 answers each. The answer should be a single word or a short phrase. There should be one correct answer only indicated by appending (CORRECT) to the end of the answer. Between each question, there should be 2 linebreaks('\n\n'). Within a question aka the question and the 4 answers, there should be 1 linebreak('\n').";

  const result = await model.generateContent([prompt, ...imageParts]);
  const response = await result.response;
  const text = response.text();
  console.log(text);
  res.json(text);
});

app.post("/upload", upload.single("pdf"), async (req, res) => {
  if (!req.file) {
    return res.status(400).send("No file uploaded.");
  }
  const pngPages = await pdfToPng(req.file.buffer, {
    // this returns an array of object where object.path gives the path and object.name gives the name
    outputFolder: "images",
  });
  console.log(pngPages);
  res.json(pngPages);
});

const start = async () => {
  app.listen(process.env.PORT, () => {
    console.log(`Listening on port ${process.env.PORT}`);
  });
};

start();
