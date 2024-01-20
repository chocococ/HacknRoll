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

app.post("/", upload.single("pdf"), async (req, res) => {
  // USING GEMINI PRO FOR TEXT ONLY
  // console.log(req.body);
  // const result = await model.generateContent(req.body.question);
  // const response = await result.response;
  // const text = response.text();
  // console.log(text);
  // res.json(text);

  // TESTING OUT GEMINI PRO VISION FOR IMAGE PROMPTS AS WELL

  if (!req.file) {
    return res.status(400).send("No file uploaded.");
  }

  const pngPages = await pdfToPng(req.file.buffer, {
    outputFolder: "images",
  });
  const imageParts = pngPages.map((page) =>
    fileToGenerativePart(page.path, "image/png")
  );

  const prompt =
    "I am studying for a quiz. This are pictures of my notes. Using these pictures and only information available in these pictures, help me to generate some questions and answers to test my knowledge. It should be in the format Question: ... Answer: ...";

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
