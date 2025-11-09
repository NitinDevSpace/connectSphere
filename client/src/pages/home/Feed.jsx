import React, { useEffect, useState, useRef } from "react";
import axiosInstance from "../../api";

const formatTimeAgo = (dateString) => {
	const now = new Date();
	const posted = new Date(dateString);
	const diffMs = now - posted;
	const diffSec = Math.floor(diffMs / 1000);
	const diffMin = Math.floor(diffSec / 60);
	const diffHr = Math.floor(diffMin / 60);
	const diffDay = Math.floor(diffHr / 24);
	const diffWeek = Math.floor(diffDay / 7);
	const diffMonth = Math.floor(diffDay / 30);
	const diffYear = Math.floor(diffDay / 365);

	if (diffSec < 60) return "Just now";
	if (diffMin < 60) return `${diffMin} minute${diffMin > 1 ? "s" : ""} ago`;
	if (diffHr < 24) return `${diffHr} hour${diffHr > 1 ? "s" : ""} ago`;
	if (diffDay < 7) return `${diffDay} day${diffDay > 1 ? "s" : ""} ago`;
	if (diffWeek < 5) return `${diffWeek} week${diffWeek > 1 ? "s" : ""} ago`;
	if (diffMonth < 12)
		return `${diffMonth} month${diffMonth > 1 ? "s" : ""} ago`;
	return `${diffYear} year${diffYear > 1 ? "s" : ""} ago`;
};

const Feed = () => {
	const [posts, setPosts] = useState([]);
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState(null);

	// Dropdown and edit modal state
	const [showDropdown, setShowDropdown] = useState(null);
	const [isEditing, setIsEditing] = useState(false);
	const [editPostData, setEditPostData] = useState({ id: null, caption: "" });
	// Dropdown toggle, edit, and delete functions
	const toggleDropdown = (postId) => {
		setShowDropdown((prev) => (prev === postId ? null : postId));
	};

	const handleEditClick = (post) => {
		setEditPostData({ id: post.id, caption: post.caption });
		setIsEditing(true);
		setShowDropdown(null);
	};

	const handleEditSubmit = async () => {
		try {
			const formData = new FormData();
			const postData = { caption: editPostData.caption };

			formData.append(
				"post",
				new Blob([JSON.stringify(postData)], { type: "application/json" })
			);

			await axiosInstance.patch(
				`/posts/${editPostData.id}`,
				formData,
				{
					headers: { "Content-Type": "multipart/form-data" },
				}
			);
			setPosts((prev) =>
				prev.map((p) =>
					p.id === editPostData.id ? { ...p, caption: editPostData.caption } : p
				)
			);
			setIsEditing(false);
		} catch (err) {
			console.error("Error updating post:", err);
		}
	};

	const handleDeletePost = async (postId) => {
		if (!window.confirm("Are you sure you want to delete this post?")) return;

		try {
			await axiosInstance.delete(
				`/posts/${postId}`
			);
			setPosts((prev) => prev.filter((p) => p.id !== postId));
		} catch (err) {
			console.error("Error deleting post:", err);
		} finally {
			setShowDropdown(null);
		}
	};

	// likes: { [postId]: { count: number, liked: boolean } }
	const [likes, setLikes] = useState({});
	// comments local count if you want to increment locally
	const [comments, setComments] = useState({});

	const [selectedPostId, setSelectedPostId] = useState(null);
	const [commentsList, setCommentsList] = useState([]);
	const [newComment, setNewComment] = useState("");
	const [commentLoading, setCommentLoading] = useState(false);

	const BASE_URL =
		"/posts/feed/recent";

	useEffect(() => {
		const fetchPosts = async () => {
			try {
				const res = await axiosInstance.get(BASE_URL);
				const data = res.data || [];

				setPosts(data);

				// Initialize likes using backend fields:
				// - count <- reactionsCount
				// - liked <- userReaction != null
				setLikes(
					data.reduce((acc, post) => {
						return {
							...acc,
							[post.id]: {
								count: post.reactionsCount ?? 0,
								liked: post.userReaction != null, // true if userReaction exists
							},
						};
					}, {})
				);

				// Initialize comments from backend if available (commentCount or commentsCount)
				setComments(
					data.reduce((acc, post) => {
						acc[post.id] = post.commentCount ?? post.commentsCount ?? 0;
						return acc;
					}, {})
				);
			} catch (err) {
				console.error("Error fetching posts:", err);
				setError("Failed to load posts.");
			} finally {
				setLoading(false);
			}
		};

		fetchPosts();
	}, []);

	const handleLike = async (postId) => {
		// Ensure safe defaults in case likes[postId] is missing for any reason
		const current = likes[postId] ?? { count: 0, liked: false };
		const originalCount = current.count;
		const originalLiked = current.liked;

		const updatedCount = originalLiked ? originalCount - 1 : originalCount + 1;
		const newLiked = !originalLiked;

		// Optimistic UI update (local only)
		setLikes((prev) => ({
			...prev,
			[postId]: {
				count: updatedCount,
				liked: newLiked,
			},
		}));

		try {
			if (!originalLiked) {
				// Like
				await axiosInstance.post(
					`/posts/${postId}/likes`,
					{ reactionType: "LIKE" }
				);
			} else {
				// Unlike
				await axiosInstance.delete(
					`/posts/${postId}/likes`
				);
			}
			// Note: on success we keep the optimistic state.
			// If you want to reconcile with server response (e.g. server returns new count),
			// you can fetch that single post and update likes[postId] accordingly.
		} catch (err) {
			console.error("Error updating like:", err);
			// Revert local optimistic update on failure
			setLikes((prev) => ({
				...prev,
				[postId]: {
					count: originalCount,
					liked: originalLiked,
				},
			}));
		}
	};

	const handleCommentClick = async (postId) => {
		setSelectedPostId(postId);
		setCommentLoading(true);
		try {
			const res = await axiosInstance.get(
				`/posts/${postId}/comments`
			);
			setCommentsList(res.data || []);
		} catch (err) {
			console.error("Error fetching comments:", err);
			setCommentsList([]);
		} finally {
			setCommentLoading(false);
		}
	};

	const handleAddComment = async () => {
		if (!newComment.trim()) return;

		const tempComment = {
			id: Date.now(),
			comment: newComment,
			userName: "You",
			commentedAt: new Date().toISOString(),
		};

		setCommentsList((prev) => [tempComment, ...prev]);
		setNewComment("");

		try {
			await axiosInstance.post(
				`/posts/${selectedPostId}/comments`,
				{ comment: tempComment.comment }
			);
		} catch (err) {
			console.error("Error adding comment:", err);
			// Revert optimistic update if needed
			setCommentsList((prev) => prev.filter((c) => c.id !== tempComment.id));
		}
	};

	const videoRefs = useRef([]);

	useEffect(() => {
		const observer = new IntersectionObserver(
			(entries) => {
				entries.forEach((entry) => {
					const video = entry.target;
					if (video.tagName === "VIDEO") {
						if (entry.isIntersecting && entry.intersectionRatio >= 0.7) {
							video.play().catch(() => {});
						} else {
							video.pause();
						}
					}
				});
			},
			{ threshold: [0.7] }
		);

		videoRefs.current.forEach((video) => {
			if (video) observer.observe(video);
		});

		return () => {
			observer.disconnect();
		};
	}, [posts]);

	if (loading)
		return (
			<div className="flex justify-center items-center h-screen">
				<p className="text-gray-500 text-lg">Loading posts...</p>
			</div>
		);

	if (error)
		return (
			<div className="flex justify-center items-center h-screen">
				<p className="text-red-500 text-lg">{error}</p>
			</div>
		);

	return (
		<div className="max-w-md mx-auto mt-6 space-y-6">
			{posts.map((post, index) => (
				<div
					key={post.id}
					className="bg-white shadow rounded-2xl overflow-hidden border border-gray-200"
				>
					<div className="flex items-center p-3">
						<img
							src={
								post.userProfilePic ||
								"https://ui-avatars.com/api/?name=User&background=random"
							}
							alt="User"
							className="w-10 h-10 rounded-full mr-3 object-cover"
						/>
						<div>
							<p className="font-semibold text-gray-800">{post.userName}</p>
							<p className="text-sm text-gray-500">
								{post.location || "Location"}
							</p>
						</div>
						<div className="ml-auto relative">
							<button
								onClick={() => toggleDropdown(post.id)}
								className="text-gray-600 hover:text-gray-900"
							>
								⋮
							</button>
							{showDropdown === post.id && (
								<div className="absolute right-0 mt-2 w-32 bg-white border rounded-lg shadow-lg z-10">
									<button
										className="block w-full text-left px-4 py-2 text-sm hover:bg-gray-100"
										onClick={() => handleEditClick(post)}
									>
										✏️ Edit
									</button>
									<button
										className="block w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-gray-100"
										onClick={() => handleDeletePost(post.id)}
									>
										🗑️ Delete
									</button>
								</div>
							)}
						</div>
					</div>
					<div className="p-4">
						<p className="text-gray-800">{post.caption}</p>
					</div>

					{/* Post Content */}
					{post.contentLink && (
						<div className="w-full max-h-[600px] flex justify-center bg-black">
							{post.fileType?.startsWith("image") ? (
								<img
									src={post.contentLink}
									alt="Post content"
									className="object-contain w-full"
									onError={(e) => (e.target.style.display = "none")}
								/>
							) : post.fileType?.startsWith("video") ? (
								<video
									src={post.contentLink}
									muted
									loop
									playsInline
									controls
									className="object-contain w-full max-h-[600px]"
									ref={(el) => (videoRefs.current[index] = el)}
								/>
							) : (
								<p className="text-gray-500 p-4">Unsupported media type</p>
							)}
						</div>
					)}

					{/* Post Buttons */}
					<div className="p-4">
						<div className="flex items-center gap-6 mt-3">
							<button
								className={`hover:scale-110 transition ${
									likes[post.id]?.liked ?? post.userReaction != null
										? "text-red-500"
										: ""
								}`}
								onClick={() => handleLike(post.id)}
							>
								{likes[post.id]?.liked ?? post.userReaction != null
									? "❤️"
									: "🖤"}{" "}
								{likes[post.id]?.count ?? post.reactionsCount ?? 0}
							</button>

							<button
								className="hover:scale-110 transition"
								onClick={() => handleCommentClick(post.id)}
							>
								💬{" "}
								{comments[post.id] ??
									post.commentCount ??
									post.commentsCount ??
									0}
							</button>
						</div>

						<p className="text-xs text-gray-400 mt-2">
							{formatTimeAgo(post.createdAt)}
						</p>
					</div>
				</div>
			))}

			{/* Comments Modal */}
			{selectedPostId && (
				<div
					className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
					onClick={() => setSelectedPostId(null)}
				>
					<div
						className="bg-white rounded-lg shadow-lg w-full max-w-md max-h-[80vh] flex flex-col"
						onClick={(e) => e.stopPropagation()}
					>
						<div className="flex justify-between items-center p-3 border-b">
							<h2 className="font-semibold text-lg">Comments</h2>
							<button onClick={() => setSelectedPostId(null)}>✖</button>
						</div>

						<div className="p-3 border-b flex items-center gap-2">
							<input
								type="text"
								value={newComment}
								onChange={(e) => setNewComment(e.target.value)}
								placeholder="Add a comment..."
								className="flex-1 border rounded-lg px-3 py-2 text-sm"
							/>
							<button
								onClick={handleAddComment}
								className="text-blue-600 font-semibold"
							>
								Post
							</button>
						</div>

						<div className="flex-1 overflow-y-auto p-3 space-y-3">
							{commentLoading ? (
								<p className="text-gray-500 text-center">Loading comments...</p>
							) : commentsList.length > 0 ? (
								commentsList.map((comment) => (
									<div key={comment.id} className="flex flex-col border-b pb-2">
										<p className="text-sm font-semibold">{comment.userName}</p>
										<p className="text-gray-700 text-sm p-2">
											{comment.comment}
										</p>
										<p className="text-xs text-gray-400">
											{formatTimeAgo(comment.commentedAt)}
										</p>
									</div>
								))
							) : (
								<p className="text-gray-500 text-center">No comments yet.</p>
							)}
						</div>
					</div>
				</div>
			)}
			{/* Edit Modal */}
			{isEditing && (
				<div
					className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
					onClick={() => setIsEditing(false)}
				>
					<div
						className="bg-white rounded-lg shadow-lg w-full max-w-md p-5"
						onClick={(e) => e.stopPropagation()}
					>
						<h2 className="text-lg font-semibold mb-3">Edit Post</h2>
						<textarea
							value={editPostData.caption}
							onChange={(e) =>
								setEditPostData({ ...editPostData, caption: e.target.value })
							}
							className="w-full border rounded-lg p-2 text-sm"
							rows="4"
						/>
						<div className="flex justify-end mt-3 gap-3">
							<button
								onClick={() => setIsEditing(false)}
								className="px-4 py-2 bg-gray-200 rounded-lg"
							>
								Cancel
							</button>
							<button
								onClick={handleEditSubmit}
								className="px-4 py-2 bg-blue-600 text-white rounded-lg"
							>
								Save
							</button>
						</div>
					</div>
				</div>
			)}
		</div>
	);
};

export default Feed;
