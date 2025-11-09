import { useState } from "react";
import PostCard from "../../components/PostCard";
import PostModal from "../../components/PostModal";
import CommentModal from "../../components/CommentsModal";
import Feed from "./Feed";
import AddPostModal from "../../components/AddPostModal";

export default function Home() {
	const [openPostModal, setOpenPostModal] = useState(false);
	const [openCommentModal, setOpenCommentModal] = useState(false);

	const posts = [];

	return (
		<div className="max-w-2xl mx-auto py-6">
			<div className="flex justify-between items-center mb-4">
				<h1 className="text-xl font-semibold">Post Feed</h1>
				<button onClick={() => setOpenPostModal(true)} className="btn">
					+ New Post
				</button>
			</div>
			{openPostModal && (
				<AddPostModal onClose={() => setOpenPostModal(false)} />
			)}
			Home Page
			<div className="space-y-4">
				<Feed />
			</div>
			<div className="space-y-4">
				{posts.map((post) => (
					<PostCard
						key={post._id}
						post={post}
						onComment={() => setOpenCommentModal(true)}
					/>
				))}
			</div>
			{openPostModal && <PostModal onClose={() => setOpenPostModal(false)} />}
			{openCommentModal && (
				<CommentModal onClose={() => setOpenCommentModal(false)} />
			)}
		</div>
	);
}
