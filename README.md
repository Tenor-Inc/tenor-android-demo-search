Tenor Android Search Demo
=========================

## Introduction
In this demo, we will look at how to create a simple GIF search app.
We will have our GIFs be searchable in three different ways:

1. **Tags**:  A display of terms with a GIF preview background.  When clicked, a search for the tag's term will occur.  
2. **Search Box**:  Searches performed by entering a search term on the EditText.
3. **Related Suggestions**:  Similar to Tags, these are displayed search terms that when clicked on will open a new search.  In the demo they appear as related terms to the search, and display above the GIFs in a horizontal scroller.

## Tenor Android Core
In order to use our API, you must have the `tenor-android-core` as part of your app.  Download the Tenor Android Core [here](https://github.com/Tenor-Inc/tenor-android-core).

### Model View Presenter (Optional)
This demo uses the MVP framework.  To read more about the MVP architectural pattern, click [here](https://en.wikipedia.org/wiki/Model-view-presenter).

_Note_, that MVP is not required for using our API.  It can work in any view structure, this demo just shows one example of how it can be used.

## Using Reaction Tags
When you first open the demo, you will be in the [MainActivity][mainactivity].  In order to get our list of tags, we will use a presenter class, [MainPresenter][mainpresenter], to make the necessary API call.

Once the API call has been constructed, we need to have its results returned, and then displayed.  In our example, [IMainView][i_mainview] provides us with callbacks necessary to return the API response to our activity.

To see a detailed look of the Tag response JSON object, click [here](https://tenor.com/gifapi#tags).

Once you have your response, we will need to load the tags into our view.  We recommend having a TextView layered on top of an ImageView, as seen in the [TagItemVH][tagitemvh].  The GIF preview background will be loaded via the `GlideTaskParams`.

The demo displays the [TagItemVH][tagitemvh] through the [TagsAdapter][tagsadapter].  When the [TagItemVH][tagitemvh] is clicked, a tag will open a [SearchActivity][searchactivity], passing the tag name through `SearchActivity.KEY_QUERY`.


## Typed Search
Using the EditText inside our [MainActivity][mainactivity], we open the [SearchActivity][searchactivity] with `EditorInfo.IME_ACTION_SEARCH`. Like tags, the query is passed to [SearchActivity][searchactivity] via `SearchActivity.KEY_QUERY`.  

Once the activity has loaded, search calls are performed through the search presenter class, [GifSearchPresenter][searchpresenter].  Callbacks are made through [IGifSearchView][i_searchview].

To see a detailed look of the Gif response JSON object, click [here](https://tenor.com/gifapi#responseobjects).

Like Tags, we will load the results returned through the API response into ImageViews, as seen in the [GifSearchItemVH][gifitemvh].
They will also be loaded using the `GlideTaskParams`.
The demo displays the [GifSearchItemVH][gifitemvh] through the [GifSearchAdapter][searchadapter].  For the demo, no full click functionality has been added.  How you wish to handle click events with GIFs is up to you.


## Suggestions (Optional)
An additional feature the demo uses in [SearchActivity][searchactivity] is to take the search query from `SearchActivity.KEY_QUERY` and call the `getSearchSuggestionsEndpoint()` in the [SearchSuggestionPresenter][suggestionpresenter].  It returns a search suggestion response (see [here](https://tenor.com/gifapi#suggestions)) and is displayed in the [SearchSuggestionAdapter][searchsuggestionadapter].

The adapter, and its view holder [SearchSuggestionVH][suggestionitemvh] are displayed as the top level element of the [GifSearchAdapter][searchadapter].  Clicking them will open a new instance of [SearchActivity][searchactivity].

Suggestions are a useful tool in the Tenor API to refine searches.  We recommend using them for a more full GIF search experience.

[mainactivity]: app/src/main/java/com/tenor/android/demo/search/activity/MainActivity.java
[mainpresenter]: app/src/main/java/com/tenor/android/demo/search/presenter/impl/MainPresenter.java
[i_mainview]: app/src/main/java/com/tenor/android/demo/search/adapter/view/IMainView.java
[tagitemvh]: app/src/main/java/com/tenor/android/demo/search/adapter/holder/TagItemVH.java
[tagsadapter]: app/src/main/java/com/tenor/android/demo/search/adapter/TagsAdapter.java

[searchactivity]: app/src/main/java/com/tenor/android/demo/search/activity/SearchActivity.java
[searchpresenter]: app/src/main/java/com/tenor/android/demo/search/presenter/impl/GifSearchPresenter.java
[i_searchview]: app/src/main/java/com/tenor/android/demo/search/adapter/view/IGifSearchView.java
[gifitemvh]: app/src/main/java/com/tenor/android/demo/search/adapter/holder/GifSearchItemVH.java
[searchadapter]: app/src/main/java/com/tenor/android/demo/search/adapter/GifSearchAdapter.java

[suggestionpresenter]: app/src/main/java/com/tenor/android/sdk/presenter/impl/SearchSuggestionPresenter.java
[suggestionitemvh]: app/src/main/java/com/tenor/android/sdk/adapter/holder/SearchSearchItemVH.java
[searchsuggestionadapter]: app/src/main/java/com/tenor/android/sdk/adapter/SearchSuggestionAdapter.java





