import React, { useState } from "react";
import axios from "axios";
import axiosInstance from "../api";

function AddPostModal({ onClose, onPostSuccess }) {
	const [caption, setCaption] = useState("");
	const [file, setFile] = useState(null);
	const [loading, setLoading] = useState(false);

	const handleFileChange = (e) => {
		setFile(e.target.files[0]);
	};

	const handleSubmit = async () => {
		if (!caption.trim() && !file) {
			alert("Please add a caption or select a file.");
			return;
		}

		setLoading(true);
		const formData = new FormData();
		const postData = {
			caption,
			description: caption, // optional
		};

		formData.append(
			"post",
			new Blob([JSON.stringify(postData)], { type: "application/json" })
		);
		if (file) {
			formData.append("file", file);
		}

		try {
			const res = await axiosInstance.post(
				"http://localhost:8080/eventsocialservice/events/1/posts",
				formData,
				{
					headers: { "Content-Type": "multipart/form-data" },
				}
			);
			// Optionally handle response
			onPostSuccess && onPostSuccess(res.data);
			setCaption("");
			setFile(null);
			onClose();
		} catch (err) {
			console.error("Error creating post:", err);
			alert("Failed to create post.");
		} finally {
			setLoading(false);
		}
	};

	return (
		<div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
			<div className="bg-white rounded-lg shadow-lg w-full max-w-md p-6">
				<div className="flex justify-between items-center mb-4">
					<h2 className="text-xl font-semibold">Create a post</h2>
					<button
						onClick={onClose}
						className="text-gray-500 hover:text-black"
						aria-label="Close"
					>
						✖
					</button>
				</div>

				<textarea
					className="w-full border rounded-md p-2 resize-none focus:outline-none"
					rows="3"
					placeholder="What do you want to talk about?"
					value={caption}
					onChange={(e) => setCaption(e.target.value)}
				/>

				{file && (
					<div className="mt-3">
						{file.type.startsWith("image") ? (
							<img
								src={URL.createObjectURL(file)}
								alt="Preview"
								className="w-full rounded-md"
							/>
						) : (
							<video
								src={URL.createObjectURL(file)}
								controls
								className="w-full rounded-md"
							/>
						)}
					</div>
				)}

				<div className="flex items-center justify-between mt-4">
					<label className="cursor-pointer text-blue-600 hover:text-blue-800">
						📎 Add Image/Video
						<input
							type="file"
							accept="image/*,video/*"
							className="hidden"
							onChange={handleFileChange}
						/>
					</label>
					<button
						onClick={handleSubmit}
						disabled={loading}
						className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 disabled:opacity-50"
					>
						{loading ? "Posting..." : "Post"}
					</button>
				</div>
			</div>
		</div>
	);
}

export default AddPostModal;
